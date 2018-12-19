package io.netstrap.core.server.websocket;

import io.netstrap.common.factory.ClassFactory;
import io.netstrap.core.server.http.mvc.controller.DefaultErrorController;
import io.netstrap.core.server.websocket.router.WebSocketAction;
import io.netstrap.core.server.websocket.stereotype.WebSocketController;
import io.netstrap.core.server.websocket.stereotype.WebSocketMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
            if (!clz.equals(DefaultErrorController.class)) {
                buildRouter(clz);
            }
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
                Class<?>[] types = buildTypes(method);
                router.setParamTypes(types);
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
