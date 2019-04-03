package io.netstrap.core;

import io.netstrap.common.factory.ClassFactory;
import io.netstrap.core.context.NetstrapSpringRunListeners;
import io.netstrap.core.server.Server;
import io.netstrap.core.context.NetstrapSpringRunListener;
import io.netstrap.core.context.stereotype.EnableNetstrapServer;
import io.netstrap.core.context.stereotype.NetstrapApplication;
import io.netstrap.core.enums.ProtocolType;
import io.netstrap.core.enums.ServerType;
import io.netstrap.core.mina.MinaServer;
import io.netstrap.core.netty.NettyServer;
import lombok.Data;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.GenericTypeResolver;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.StandardEnvironment;
import org.springframework.util.Assert;
import org.springframework.util.ReflectionUtils;
import org.springframework.util.StopWatch;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static io.netstrap.common.NetstrapConstant.DEFAULT_SCAN;

/**
 * Netstrap启动类
 *
 * @author minghu.zhang
 * @date 2018/11/03
 */
@Log4j2
@Data
public class NetstrapBootApplication {

    /**
     * spring上下文
     */
    private ConfigurableApplicationContext context;
    /**
     * 启动主类
     */
    private Class<?> clz;
    /**
     * 网络服务框架
     */
    private Server server;
    /**
     * 类工厂
     */
    private ClassFactory factory;

    /**
     * 所需扫描的一组package
     */
    private String[] basePackages;
    /**
     * Spring应用初始化类
     */
    private List<ApplicationContextInitializer<?>> initialises = new ArrayList<>();
    /**
     * Spring应用事件监听类
     */
    private List<ApplicationListener<?>> listeners = new ArrayList<>();

    /**
     * 构建启动类
     */
    private NetstrapBootApplication(Class<?> clz) {
        this.clz = clz;
        initBaseScanPackages();
        //获取类加载器
        this.factory = ClassFactory.getInstance(basePackages);
        //设置容器初始化类和监听类
        setInitializerAndListeners();
    }

    /**
     * 启动Netstrap服务
     */
    public static ConfigurableApplicationContext run(Class<?> clz, String[] args) {
        if (!clz.isAnnotationPresent(NetstrapApplication.class)) {
            throw new RuntimeException("Please check your Annotation \"@NetstrapApplication\"  And add it!");
        }

        return new NetstrapBootApplication(clz).run(clz.getAnnotation(NetstrapApplication.class).configLocations());
    }

    /**
     * 设置容器初始化类和监听类
     */
    private void setInitializerAndListeners() {
        try {
            List<Class<?>> initializerClassList = factory.getClassByInterface(ApplicationContextInitializer.class);
            for (Class<?> clz : initializerClassList) {
                initialises.add((ApplicationContextInitializer) clz.newInstance());
            }

            List<Class<?>> listenerClassList = factory.getClassByInterface(ApplicationListener.class);
            for (Class<?> clz : listenerClassList) {
                listeners.add((ApplicationListener) clz.newInstance());
            }

        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置类工厂扫描路径
     */
    private void initBaseScanPackages() {
        NetstrapApplication annotation =
                clz.getAnnotation(NetstrapApplication.class);
        List<String> packages = new ArrayList<>(Arrays.asList(DEFAULT_SCAN));
        packages.addAll(Arrays.asList(annotation.packages()));
        packages.add(clz.getPackage().getName());
        basePackages = packages.toArray(new String[]{});
    }


    /**
     * 装配Spring容器
     */
    private ConfigurableApplicationContext run(String[] configLocations) {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        NetstrapSpringRunListeners listeners = getRunListener();
        /**
         * starting事件表明，Spring容器将会进行初始化，在此之前，我们可以做一些事情
         * 比如注册一些类型转换器：ConvertUtils.register(Converter converter, Class<?> clazz)
         */
        listeners.starting();
        try {

            //创建上下文
            context = createApplicationContext(configLocations);
            //准备Context
            prepareContext(context, new StandardEnvironment());
            //Spring容器初始化完毕（包括引入的Spring组件）之后调用
            listeners.contextPrepare(context);
            //启动网络服务
            networkServiceStartup(context);
            //停止监控
            stopWatch.stop();
            //Spring容器初始化完毕并且启动网络服务之后调用
            listeners.started(context);
            //开始监听请求
            server.join();
        } catch (Throwable ex) {
            ex.printStackTrace();
            context.close();
            if (server.isStarted()) {
                server.stop();
            }
            handleRunFailure(context, ex, listeners);
        }

        return context;
    }

    /**
     * 启动网络服务
     */
    private void networkServiceStartup(ConfigurableApplicationContext context) throws InterruptedException {

        EnableNetstrapServer enableServer;
        ServerType serverType;
        ProtocolType protocol;

        if (clz.isAnnotationPresent(EnableNetstrapServer.class)) {
            enableServer = clz.getAnnotation(EnableNetstrapServer.class);
            serverType = enableServer.serverType();
            protocol = enableServer.protocol();
        } else {
            serverType = ServerType.Netty;
            protocol = ProtocolType.HTTP;
        }

        if (serverType.equals(ServerType.Netty)) {
            server = context.getBean(NettyServer.class);
        } else if (serverType.equals(ServerType.Mina)) {
            server = context.getBean(MinaServer.class);
        }

        server.start(protocol);
        log.info("The network service[" + serverType + " -> " + protocol + "] is starting.");
    }

    /**
     * 装配SpringContext
     * 设置环境，初始化调用，设置监听器
     */
    private void prepareContext(ConfigurableApplicationContext context, ConfigurableEnvironment environment) {
        context.setEnvironment(environment);
        applyInitializer(context);
        for (ApplicationListener listener : listeners) {
            context.addApplicationListener(listener);
        }
    }


    /**
     * 执行Spring初始化方法
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    private void applyInitializer(ConfigurableApplicationContext context) {
        for (ApplicationContextInitializer initializer : getInitialises()) {
            Class<?> requiredType = GenericTypeResolver.resolveTypeArgument(
                    initializer.getClass(), ApplicationContextInitializer.class);
            assert requiredType != null;
            Assert.isInstanceOf(requiredType, context, "Unable to call initializer.");
            initializer.initialize(context);
        }
    }

    /**
     * 创建Spring容器
     */
    private ConfigurableApplicationContext createApplicationContext(String[] configLocations) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.setParent(new ClassPathXmlApplicationContext(configLocations));
        context.scan(DEFAULT_SCAN);
        context.refresh();
        return context;
    }

    /**
     * 获取Spring运行监听器
     */
    private NetstrapSpringRunListeners getRunListener() {

        NetstrapSpringRunListeners netstrapSpringRunListeners;
        try {
            List<Class<?>> listenerClasses = factory.getClassByInterface(NetstrapSpringRunListener.class);

            List<NetstrapSpringRunListener> listeners = new ArrayList<>();
            for (Class<?> clz : listenerClasses) {
                Constructor<?> constructor = clz.getConstructor(NetstrapBootApplication.class);
                listeners.add((NetstrapSpringRunListener) constructor.newInstance(this));
            }

            AnnotationAwareOrderComparator.sort(listeners);

            netstrapSpringRunListeners = new NetstrapSpringRunListeners(listeners);
        } catch (Exception e) {
            log.error(e.getMessage());
            netstrapSpringRunListeners = new NetstrapSpringRunListeners();
        }

        return netstrapSpringRunListeners;
    }

    /**
     * 异常处理
     */
    private void handleRunFailure(ConfigurableApplicationContext context,
                                  Throwable exception,
                                  NetstrapSpringRunListeners listeners) {
        try {
            if (listeners != null) {
                listeners.failed(context, exception);
            }
        } catch (Exception ex) {
            log.warn("Unable to close ApplicationContext", ex);
        } finally {
            if (context != null) {
                context.close();
            }
        }

        ReflectionUtils.rethrowRuntimeException(exception);
    }

}
