package io.netstrap.core.server.mvc.router;

import io.netstrap.common.factory.ClassFactory;
import io.netstrap.core.server.http.HttpMethod;
import io.netstrap.core.server.mvc.controller.DefaultErrorController;
import io.netstrap.core.server.mvc.stereotype.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 路由工厂
 *
 * @author minghu.zhang
 * @date 2018/11/09
 */
@Component
public class RouterFactory {

    /**
     * 调用模型
     */
    private final static Map<String, InvokeAction> ROUTERS = new HashMap<>(8);

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
    private RouterFactory(ApplicationContext context) {
        this.context = context;
        this.factory = ClassFactory.getInstance();
    }

    /**
     * 初始化MVC
     */
    @PostConstruct
    private RouterFactory init() {
        initDefault();
        initRouter();
        return this;
    }

    /**
     * 初始化默认路由
     */
    private void initDefault() {
        buildRouter(DefaultErrorController.class);
    }

    /**
     * 初始化控制器
     */
    private void initRouter() {
        List<Class<?>> controllers = factory.getClassByAnnotation(RestController.class);
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
        if (clz.isAnnotationPresent(RequestMapping.class)) {
            groupUri = clz.getDeclaredAnnotation(RequestMapping.class).value();
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
     * 构建参数对象
     */
    private void buildArguments(Method method) {
        DefaultParameterNameDiscoverer discover = new DefaultParameterNameDiscoverer();
        String[] parameterNames = discover.getParameterNames(method);
        //TODO 构建参数对象
        for (String name : parameterNames) {

        }
    }

    /**
     * 构建路由对象
     */
    private void buildMethod(Object invoker, Method method, String groupUri, String slash) {
        InvokeAction router = new InvokeAction();
        router.setInvoker(invoker);
        method.setAccessible(true);

        RequestMapping mapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        assert mapping != null;

        HttpMethod[] httpMethods = mapping.method();
        String mappingUri = mapping.value();

        if (!StringUtils.isEmpty(mappingUri)) {
            mappingUri = (mappingUri.startsWith(slash) ? mappingUri : slash + mappingUri);
            router.setAction(method);
            router.setMethods(httpMethods);
            router.setUri(groupUri + mappingUri);
            put(router.getUri(), router);
        }
    }

    /**
     * 添加路由模型
     */
    private void put(String uri, InvokeAction router) {
        ROUTERS.put(uri, router);
    }

    /**
     * 获取路由
     */
    public InvokeAction get(String uri) {

        InvokeAction router;
        if (!ROUTERS.containsKey(uri)) {
            router = getNotFoundRouter();
        } else {
            router = ROUTERS.get(uri);
        }

        return router;
    }

    /**
     * 405
     */
    public InvokeAction getMethodNotAllowedRouter() {
        return get(DefaultErrorController.METHOD_NOT_ALLOWED);
    }

    /**
     * 500
     */
    public InvokeAction getInternalServiceErrorRouter() {
        return get(DefaultErrorController.INTERNAL_SERVICE_ERROR);
    }

    /**
     * 400
     */
    public InvokeAction getBadRequestRouter() {
        return get(DefaultErrorController.BAD_REQUEST);
    }

    /**
     * 404
     */
    public InvokeAction getNotFoundRouter() {
        return get(DefaultErrorController.NOT_FOUND);
    }

    /**
     * 403
     */
    public InvokeAction getForbiddenRouter() {
        return get(DefaultErrorController.FORBIDDEN);
    }

    /**
     * 401
     */
    public InvokeAction getUnauthorizedRouter() {
        return get(DefaultErrorController.UNAUTHORIZED);
    }
}
