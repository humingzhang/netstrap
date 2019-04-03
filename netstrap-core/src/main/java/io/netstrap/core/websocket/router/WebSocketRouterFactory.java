package io.netstrap.core.websocket.router;

import io.netstrap.common.factory.ClassFactory;
import io.netstrap.core.websocket.stereotype.mapping.WebSocketController;
import io.netstrap.core.websocket.stereotype.mapping.WebSocketMapping;
import io.netstrap.core.websocket.stereotype.parameter.WebSocketValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.*;

/**
 * 调用器工厂
 *
 * @author minghu.zhang
 * @date 2018/12/19 14:45
 */
@Component
public class WebSocketRouterFactory {

    /**
     * 调用模型
     */
    private final static Map<String, WebSocketAction> ROUTERS = new HashMap<>(8);

    /**
     * 类工厂
     */
    private ClassFactory factory;

    /**
     * Spring上下文
     */
    private ApplicationContext context;

    /**
     * 构造函数
     */
    @Autowired
    private WebSocketRouterFactory(ApplicationContext context) {
        this.context = context;
        this.factory = ClassFactory.getInstance();
    }

    /**
     * 初始化MVC
     */
    @PostConstruct
    private WebSocketRouterFactory init() {
        initRouter();
        return this;
    }

    /**
     * 初始化控制器
     */
    private void initRouter() {
        List<Class<?>> controllers = factory.getClassByAnnotation(WebSocketController.class);
        for (Class clz : controllers) {
            buildRouter(clz);
        }
    }

    /**
     * 构建路由对象
     */
    private void buildRouter(Class<?> clz) {
        Object invoker = context.getBean(clz);

        String groupUri = "";
        if (clz.isAnnotationPresent(WebSocketMapping.class)) {
            groupUri = clz.getDeclaredAnnotation(WebSocketMapping.class).value();
        }

        String slash = "/";

        if (!StringUtils.isEmpty(groupUri) && !groupUri.startsWith(slash)) {
            groupUri = slash + groupUri;
        }
        Method[] declaredMethods = clz.getDeclaredMethods();
        for (Method method : declaredMethods) {
            buildMethod(invoker, method, groupUri, slash);
        }
    }

    /**
     * 构建路由对象
     */
    private void buildMethod(Object invoker, Method method, String groupUri, String slash) {
        WebSocketAction router = new WebSocketAction();
        router.setInvoker(invoker);
        method.setAccessible(true);

        WebSocketMapping mapping = AnnotatedElementUtils.findMergedAnnotation(method, WebSocketMapping.class);
        if (Objects.nonNull(mapping)) {
            String mappingUri = mapping.value();
            if (!StringUtils.isEmpty(mappingUri)) {
                mappingUri = (mappingUri.startsWith(slash) ? mappingUri : slash + mappingUri);
                router.setAction(method);
                router.setUri(groupUri + mappingUri);
                put(router.getUri(), router);
                router.setMappings(buildArguments(method));
            }
        }
    }

    /**
     * 添加路由模型
     */
    private void put(String uri, WebSocketAction router) {
        ROUTERS.put(uri, router);
    }

    /**
     * 获取路由对象
     *
     * @param uri uri
     * @return 路由对象
     */
    public WebSocketAction get(String uri) {
        return ROUTERS.get(uri);
    }

    /**
     * 获取参数映射数组
     * @param method java 方法
     * @return 参数映射数组
     */
    private WebSocketParamMapping[] buildArguments(Method method) {
        List<WebSocketParamMapping> mappings = new ArrayList<>(8);

        //指定默认参数名，并创建mapping对象
        DefaultParameterNameDiscoverer discover = new DefaultParameterNameDiscoverer();
        String[] parameterNames = discover.getParameterNames(method);
        assert parameterNames != null;
        for (String name : parameterNames) {
            WebSocketParamMapping mapping = new WebSocketParamMapping();
            mapping.setAlisName(name);
            mappings.add(mapping);
        }

        //参数映射
        buildParamTypeMapping(method, mappings);

        return mappings.toArray(new WebSocketParamMapping[]{});

    }

    /**
     * 构建参数类型
     */
    private void buildParamTypeMapping(Method method, List<WebSocketParamMapping> mappings) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            WebSocketParamMapping mapping = mappings.get(i);
            buildParamMapping(parameter, mapping);
            Class<?> type = parameter.getType();
            mapping.setParamClass(type);
        }
    }

    /**
     * 构建参数对象
     */
    private void buildParamMapping(Parameter parameter, WebSocketParamMapping mapping) {
        //指定参数别名
        WebSocketValue alias = AnnotatedElementUtils.findMergedAnnotation(parameter, WebSocketValue.class);
        String aliasName = "";

        WebSocketContextType contextType;
        if (Objects.nonNull(alias)) {
            aliasName = alias.value();
            contextType = alias.type();
        } else {
            contextType = WebSocketContextType.REQUEST_PARAM;
        }

        if (!StringUtils.isEmpty(aliasName)) {
            mapping.setAlisName(aliasName);
        }

        mapping.setContextType(contextType);
    }

    /**
     * 构建参数对象
     */
    private Class<?>[] buildTypes(Method method) {

        List<Class<?>> types = new ArrayList<>(8);

        for (Parameter parameter : method.getParameters()) {
            types.add(parameter.getType());
        }

        return types.toArray(new Class<?>[]{});
    }

}
