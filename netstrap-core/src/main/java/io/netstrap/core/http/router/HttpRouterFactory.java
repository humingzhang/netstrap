package io.netstrap.core.http.router;

import io.netstrap.common.factory.ClassFactory;
import io.netstrap.common.tool.Convertible;
import io.netstrap.core.http.controller.DefaultErrorController;
import io.netstrap.core.http.stereotype.RestController;
import io.netstrap.core.http.stereotype.mapping.RequestMapping;
import io.netstrap.core.http.stereotype.parameter.RequestValue;
import io.netstrap.core.http.ErrorPath;
import io.netty.handler.codec.http.multipart.MixedFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * 路由工厂
 *
 * @author minghu.zhang
 * @date 2018/11/09
 */
@Component
public class HttpRouterFactory {

    /**
     * 调用模型
     */
    private final static Map<String, HttpAction> ROUTERS = new HashMap<>(8);

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
    private HttpRouterFactory(ApplicationContext context) {
        this.context = context;
        this.factory = ClassFactory.getInstance();
    }

    /**
     * 初始化MVC
     */
    @PostConstruct
    private HttpRouterFactory init() {
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
     * 构建路由对象
     */
    private void buildMethod(Object invoker, Method method, String groupUri, String slash) {
        HttpAction router = new HttpAction();
        router.setInvoker(invoker);
        method.setAccessible(true);

        RequestMapping mapping = AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
        if (Objects.nonNull(mapping)) {
            HttpMethod[] httpMethods = mapping.method();
            String mappingUri = mapping.value();

            if (!StringUtils.isEmpty(mappingUri)) {
                mappingUri = (mappingUri.startsWith(slash) ? mappingUri : slash + mappingUri);
                router.setAction(method);
                router.setMethods(httpMethods);
                router.setUri(groupUri + mappingUri);
                put(router.getUri(), router);
                HttpParamMapping[] mappings = buildArguments(method);
                router.setMappings(mappings);
            }
        }
    }

    /**
     * 构建参数对象
     * 解析参数来源，封装处理类
     */
    private HttpParamMapping[] buildArguments(Method method) {

        List<HttpParamMapping> mappings = new ArrayList<>(8);

        //指定默认参数名，并创建mapping对象
        DefaultParameterNameDiscoverer discover = new DefaultParameterNameDiscoverer();
        String[] parameterNames = discover.getParameterNames(method);
        assert parameterNames != null;
        for (String name : parameterNames) {
            HttpParamMapping mapping = new HttpParamMapping();
            mapping.setAlisName(name);
            mappings.add(mapping);
        }

        //参数映射
        buildParamTypeMapping(method, mappings);
        //构建泛型映射
        buildGenericMapping(method, mappings);

        return mappings.toArray(new HttpParamMapping[]{});
    }

    /**
     * 构建参数类型
     */
    private void buildParamTypeMapping(Method method, List<HttpParamMapping> mappings) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            HttpParamMapping mapping = mappings.get(i);
            buildParamMapping(parameter, mapping);
            Class<?> type = parameter.getType();
            mapping.setParamClass(type);
            if (parameter.getType().isArray()) {
                Class<?> componentType = parameter.getType().getComponentType();
                mapping.setParamType(HttpParamType.ARRAY_PARAM);
                mapping.setParamClass(componentType);
            } else if (Convertible.convertible(type)) {
                mapping.setParamType(HttpParamType.BASE_PARAM);
            } else if (type.equals(MixedFileUpload.class)) {
                mapping.setParamType(HttpParamType.FILE_PARAM);
            } else if (List.class.isAssignableFrom(type)) {
                mapping.setParamType(HttpParamType.LIST_PARAM);
            } else {
                mapping.setParamType(HttpParamType.OTHER_PARAM);
            }
        }
    }

    /**
     * 构建泛型对象
     */
    private void buildGenericMapping(Method method, List<HttpParamMapping> mappings) {

        Type[] parameterTypes = method.getGenericParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Type parameterType = parameterTypes[i];
            if (parameterType instanceof ParameterizedType) {
                Type[] actualType = ((ParameterizedType) parameterType).getActualTypeArguments();
                if (actualType.length > 0) {
                    HttpParamMapping mapping = mappings.get(i);
                    mapping.setGenericType((Class<?>) actualType[0]);
                }
            }
        }
    }

    /**
     * 构建参数对象
     */
    private void buildParamMapping(Parameter parameter, HttpParamMapping mapping) {
        //指定参数别名
        RequestValue alias = AnnotatedElementUtils.findMergedAnnotation(parameter, RequestValue.class);
        String aliasName = "";
        HttpContextType contextType;
        if (Objects.nonNull(alias)) {
            aliasName = alias.value();
            contextType = alias.type();
        } else {
            contextType = HttpContextType.REQUEST_PARAM;
        }

        if (!StringUtils.isEmpty(aliasName)) {
            mapping.setAlisName(aliasName);
        }

        mapping.setContextType(contextType);
    }

    /**
     * 添加路由模型
     */
    private void put(String uri, HttpAction router) {
        ROUTERS.put(uri, router);
    }

    /**
     * 获取路由
     */
    public HttpAction get(String uri) {

        HttpAction router;
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
    public HttpAction getMethodNotAllowedRouter() {
        return get(ErrorPath.METHOD_NOT_ALLOWED);
    }

    /**
     * 500
     */
    public HttpAction getInternalServiceErrorRouter() {
        return get(ErrorPath.INTERNAL_SERVICE_ERROR);
    }

    /**
     * 400
     */
    public HttpAction getBadRequestRouter() {
        return get(ErrorPath.BAD_REQUEST);
    }

    /**
     * 404
     */
    private HttpAction getNotFoundRouter() {
        return get(ErrorPath.NOT_FOUND);
    }

    /**
     * 403
     */
    public HttpAction getForbiddenRouter() {
        return get(ErrorPath.FORBIDDEN);
    }

    /**
     * 401
     */
    public HttpAction getUnauthorizedRouter() {
        return get(ErrorPath.UNAUTHORIZED);
    }
}
