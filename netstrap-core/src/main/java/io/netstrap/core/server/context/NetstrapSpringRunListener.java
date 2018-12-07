package io.netstrap.core.server.context;

import org.springframework.context.ConfigurableApplicationContext;

/**
 * Netstrap运行监听处理类，Spring容器启动的各个状态会以事件的形式通知Spring的监听器，参考SpringApplicationRunListener实现
 * starting->environmentPrepared->contextPrepared->contextLoaded->started->running->failed
 *
 * @author minghu.zhang
 * @date 2018/11/03
 */
public interface NetstrapSpringRunListener {

    /**
     * 系统启动之前调用
     */
    void starting();

    /**
     * Spring容器初始化完毕（包括引入的Spring组件）之后调用
     *
     * @param context Spring上下文
     */
    void contextPrepare(ConfigurableApplicationContext context);

    /**
     * Spring容器初始化完毕并且启动网络服务之后调用
     *
     * @param context Spring上下文
     */
    void started(ConfigurableApplicationContext context);

    /**
     * 启动异常后调用，执行异常处理操作（关闭端口，停止容器，释放资源等）
     *
     * @param context   Spring上下文
     * @param exception 异常对象
     */
    void failed(ConfigurableApplicationContext context, Throwable exception);
}
