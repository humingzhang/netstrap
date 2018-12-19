package io.netstrap.core.server.http.mvc.dispatcher;

import io.netstrap.common.tool.Convertible;
import io.netstrap.common.tool.JsonTool;
import io.netstrap.core.server.exception.ParameterParseException;
import io.netstrap.core.server.http.HttpMethod;
import io.netstrap.core.server.http.datagram.AbstractHttpRequest;
import io.netstrap.core.server.http.datagram.AbstractHttpResponse;
import io.netstrap.core.server.http.wrapper.HttpBody;
import io.netstrap.core.server.http.wrapper.HttpForm;
import io.netstrap.core.server.http.mvc.AbstractDispatcher;
import io.netstrap.core.server.http.mvc.filter.DefaultWebFilter;
import io.netstrap.core.server.http.mvc.router.HttpAction;
import io.netstrap.core.server.http.mvc.router.ParamMapping;
import io.netstrap.core.server.http.mvc.router.RouterFactory;
import io.netty.handler.codec.http.multipart.MixedFileUpload;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 默认Http请求分发
 *
 * @author minghu.zhang
 * @date 2018/11/08
 */
@Component
public class DefaultHttpDispatcher extends AbstractDispatcher {

    /**
     * 路由工厂
     */
    private final RouterFactory factory;

    /**
     * 构造函数注入
     */
    @Autowired
    public DefaultHttpDispatcher(DefaultWebFilter webFilter, RouterFactory factory) {
        super(webFilter);
        this.factory = factory;
    }

    @Override
    protected void dispatcher(AbstractHttpRequest request, AbstractHttpResponse response) {
        String uri = request.getRequestContext().get("uri");
        HttpAction router = factory.get(uri);
        if (router.getUri().equals(uri)) {
            //检查method
            HttpMethod requestMethod = request.getMethod();
            boolean methodNotAllowed = true;
            for (HttpMethod method : router.getMethods()) {
                if (method.equals(requestMethod)) {
                    methodNotAllowed = false;
                    break;
                }
            }
            if (methodNotAllowed) {
                router = factory.getMethodNotAllowedRouter();
            }
        }

        doInvoke(router, request, response);
    }

    /**
     * 执行调用
     */
    private void doInvoke(HttpAction router, AbstractHttpRequest request, AbstractHttpResponse response) {
        //执行调用
        final Object result;
        try {
            Object[] parameters = parseParameter(router.getMappings(), request, response);
            result = router.getAction().invoke(router.getInvoker(), parameters);
            if (Objects.nonNull(result)) {
                byte[] body;
                if (Convertible.convertible(result.getClass())) {
                    body = ((String) result).getBytes();
                } else {
                    body = JsonTool.obj2json(result).getBytes();
                }
                response.setBody(HttpBody.wrap(body));
            }
        } catch (Exception e) {
            request.addAttribute("message", e.getMessage());
            if (e instanceof ParameterParseException) {
                doInvoke(factory.getBadRequestRouter(), request, response);
            } else {
                doInvoke(factory.getInternalServiceErrorRouter(), request, response);
            }
        }
    }

    /**
     * 解析调用参数
     */
    private Object[] parseParameter(ParamMapping[] mappings, AbstractHttpRequest request, AbstractHttpResponse response) {
        Object[] parameters = new Object[mappings.length];

        try {
            for (int i = 0; i < mappings.length; i++) {
                ParamMapping mapping = mappings[i];
                Class<?> paramClass = mapping.getParamClass();
                if (paramClass.equals(AbstractHttpRequest.class)) {
                    parameters[i] = request;
                    continue;
                }

                if (paramClass.equals(AbstractHttpResponse.class)) {
                    parameters[i] = response;
                    continue;
                }
                //解析参数
                parameters[i] = parseValue(mapping, request, paramClass);
            }

        } catch (Exception e) {
            throw new ParameterParseException(e.getMessage());
        }

        return parameters;
    }

    /**
     * 类型转换
     */
    private Object convertValueType(Object baseValue, Class<?> type) {
        Object value = null;
        if (Objects.nonNull(baseValue)) {
            if (type.equals(String.class)) {
                value = baseValue;
            } else {
                value = ConvertUtils.convert(baseValue, type);
            }
        }
        return value;
    }

    /**
     * 解析参数值
     */
    private Object parseValue(ParamMapping mapping, AbstractHttpRequest request, Class<?> paramClass) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        String alias = mapping.getAlisName();
        Object value = null;

        switch (mapping.getContextType()) {
            case REQUEST_PARAM:
                value = simpleParamParse(paramClass, alias, request.getRequestParam());
                break;
            case REQUEST_HEADER:
                value = simpleParamParse(paramClass, alias, request.getRequestHeader());
                break;
            case REQUEST_CONTEXT:
                value = simpleParamParse(paramClass, alias, request.getRequestContext());
                break;
            case REQUEST_ATTRIBUTE:
                value = simpleParamParse(paramClass, alias, request.getRequestAttribute());
                break;
            case REQUEST_FORM:
                value = formParse(mapping, request);
                break;
            case REQUEST_BODY:
                if (request.getMethod().equals(HttpMethod.POST)) {
                    Object baseValue = request.getRequestBody().getString();
                    value = JsonTool.json2obj(baseValue.toString(), paramClass);
                }
                break;
            default:
                break;
        }

        return value;
    }

    /**
     * 构造简单参数
     */
    private Object simpleParamParse(Class<?> paramClass, String alias, Map<String, ?> source)
            throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object value;
        if (Convertible.convertible(paramClass)) {
            value = convertValueType(source.get(alias), paramClass);
        } else {
            value = paramClass.newInstance();
            BeanUtils.copyProperties(value, source);
        }
        return value;
    }

    /**
     * 表单参数解析
     */
    private Object formParse(ParamMapping mapping, AbstractHttpRequest request) {
        Object value = null;

        if (request.getMethod().equals(HttpMethod.POST)) {
            String alias = mapping.getAlisName();
            Class<?> paramClass = mapping.getParamClass();
            HttpForm requestForm = request.getRequestForm();
            switch (mapping.getParamType()) {
                case BASE_PARAM:
                    value = convertValueType(requestForm.getParam(alias), paramClass);
                    break;
                case FILE_PARAM:
                    value = requestForm.getUpload(alias);
                    break;
                case LIST_PARAM:
                    value = handleFormList(mapping.getGenericType(), requestForm, alias, paramClass);
                    break;
                case ARRAY_PARAM:
                    value = handleFormArray(requestForm, alias, paramClass);
                    break;
                default:
                    break;
            }
        }

        return value;
    }

    /**
     * 处理List
     */
    private Object handleFormList(Class<?> genericType, HttpForm requestForm, String alias, Class<?> paramClass) {
        Object value = null;

        if (genericType.equals(MixedFileUpload.class)) {
            value = requestForm.getUploads(alias);
        } else if (Convertible.convertible(genericType)) {
            List<String> params = requestForm.getParams(alias);
            if (genericType.equals(String.class)) {
                value = params;
            } else {
                List<Object> values = new ArrayList<>();
                for (String param : params) {
                    values.add(convertValueType(param, genericType));
                }
                value = paramClass.cast(values);
            }
        }

        return value;
    }

    /**
     * 处理数组
     */
    private Object handleFormArray(HttpForm requestForm, String alias, Class<?> paramClass) {
        Object value;

        if (paramClass.equals(MixedFileUpload.class)) {
            value = requestForm.getUploads(alias).toArray(new MixedFileUpload[]{});
        } else {
            List<String> params = requestForm.getParams(alias);
            Object array = Array.newInstance(paramClass, params.size());

            for (int i = 0; i < params.size(); i++) {
                String param = params.get(i);
                Array.set(array, i, convertValueType(param, paramClass));
            }

            value = array;
        }

        return value;
    }

}
