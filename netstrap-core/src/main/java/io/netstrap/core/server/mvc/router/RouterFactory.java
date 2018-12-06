package io.netstrap.core.server.mvc.router;

import io.netstrap.common.factory.ClassFactory;
import io.netstrap.common.tool.Convertible;
import io.netstrap.core.server.http.HttpMethod;
import io.netstrap.core.server.http.ContextType;
import io.netstrap.core.server.http.ParamType;
import io.netstrap.core.server.mvc.controller.DefaultErrorController;
import io.netstrap.core.server.mvc.stereotype.*;
import io.netstrap.core.server.mvc.stereotype.mapping.RequestMapping;
import io.netstrap.core.server.mvc.stereotype.parameter.NameAlias;
import io.netty.handler.codec.http.multipart.MixedFileUpload;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.lang.reflect.*;
import java.util.*;

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
     * 构建路由对象
     */
    private void buildMethod(Object invoker, Method method, String groupUri, String slash) {
        InvokeAction router = new InvokeAction();
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
                ParamMapping[] mappings = buildArguments(method);
                router.setMappings(mappings);
            }
        }
    }

    /**
     * 构建参数对象
     * 解析参数来源，封装处理类
     */
    private ParamMapping[] buildArguments(Method method) {

        List<ParamMapping> mappings = new ArrayList<>(8);

        //指定默认参数名，并创建mapping对象
        DefaultParameterNameDiscoverer discover = new DefaultParameterNameDiscoverer();
        String[] parameterNames = discover.getParameterNames(method);
        assert parameterNames != null;
        for (String name : parameterNames) {
            ParamMapping mapping = new ParamMapping();
            mapping.setAlisName(name);
            mappings.add(mapping);
        }

        //参数映射
        buildParamTypeMapping(method,mappings);
        //构建泛型映射
        buildGenericMapping(method,mappings);

        return mappings.toArray(new ParamMapping[]{});
    }

    /**
     * 构建参数类型
     */
    private void buildParamTypeMapping(Method method,List<ParamMapping> mappings) {
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            Parameter parameter = parameters[i];
            ParamMapping mapping = mappings.get(i);
            buildParamMapping(parameter, mapping);
            Class<?> type = parameter.getType();
            mapping.setParamClass(type);
            if (parameter.getType().isArray()) {
                Class<?> componentType = parameter.getType().getComponentType();
//                Object array = Array.newInstance(componentType, 2);
//                // 赋值
//                Array.set(array, 0, 1);
//                Array.set(array, 1, 2);
                mapping.setParamType(ParamType.ARRAY_PARAM);
                mapping.setParamClass(componentType);
            } else if (Convertible.convertible(type)) {
                mapping.setParamType(ParamType.BASE_PARAM);
            } else if (type.equals(MixedFileUpload.class)) {
                mapping.setParamType(ParamType.FILE_PARAM);
            } else if (List.class.isAssignableFrom(type)) {
                mapping.setParamType(ParamType.LIST_PARAM);
            } else {
                mapping.setParamType(ParamType.OTHER_PARAM);
            }
        }
    }

    /**
     * 构建泛型对象
     */
    private void buildGenericMapping(Method method,List<ParamMapping> mappings) {

        Type[] parameterTypes = method.getGenericParameterTypes();
        for (int i = 0; i < parameterTypes.length; i++) {
            Type parameterType = parameterTypes[i];
            if (parameterType instanceof ParameterizedType) {
                Type[] actualType = ((ParameterizedType) parameterType).getActualTypeArguments();
                if (actualType.length > 0) {
                    ParamMapping mapping = mappings.get(i);
                    mapping.setGenericType((Class<?>) actualType[0]);
                }
            }
        }
    }

    /**
     * 构建参数对象
     */
    private void buildParamMapping(Parameter parameter, ParamMapping mapping) {
        //指定参数别名
        NameAlias alias = AnnotatedElementUtils.findMergedAnnotation(parameter, NameAlias.class);
        String aliasName = "";
        ContextType contextType;
        if (Objects.nonNull(alias)) {
            aliasName = alias.value();
            contextType = alias.type();
        } else {
            contextType = ContextType.REQUEST_PARAM;
        }

        if (!StringUtils.isEmpty(aliasName)) {
            mapping.setAlisName(aliasName);
        }

        mapping.setContextType(contextType);
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
