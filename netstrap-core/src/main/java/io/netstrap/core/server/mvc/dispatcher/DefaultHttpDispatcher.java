package io.netstrap.core.server.mvc.dispatcher;

import io.netstrap.common.tool.Convertible;
import io.netstrap.common.tool.JsonTool;
import io.netstrap.core.server.exception.ParameterParseException;
import io.netstrap.core.server.http.HttpMethod;
import io.netstrap.core.server.http.datagram.HttpRequest;
import io.netstrap.core.server.http.datagram.HttpResponse;
import io.netstrap.core.server.http.wrapper.HttpBody;
import io.netstrap.core.server.mvc.Dispatcher;
import io.netstrap.core.server.mvc.filter.DefaultWebFilter;
import io.netstrap.core.server.mvc.router.InvokeAction;
import io.netstrap.core.server.mvc.router.ParamMapping;
import io.netstrap.core.server.mvc.router.RouterFactory;
import io.netty.handler.codec.http.multipart.MixedFileUpload;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 默认Http请求分发
 *
 * @author minghu.zhang
 * @date 2018/11/08
 */
@Component
public class DefaultHttpDispatcher extends Dispatcher {

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
    protected void dispatcher(HttpRequest request, HttpResponse response) {
        String uri = request.getRequestContext().get("uri");
        InvokeAction router = factory.get(uri);
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
    private void doInvoke(InvokeAction router, HttpRequest request, HttpResponse response) {
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
            request.setAttribute("message", e.getMessage());
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
    private Object[] parseParameter(ParamMapping[] mappings, HttpRequest request, HttpResponse response) {
        Object[] parameters = new Object[mappings.length];

        try {
            for (int i = 0; i < mappings.length; i++) {
                ParamMapping mapping = mappings[i];
                Class<?> type = mapping.getType();
                if (type.equals(HttpRequest.class)) {
                    parameters[i] = request;
                    continue;
                }

                if (type.equals(HttpResponse.class)) {
                    parameters[i] = response;
                    continue;
                }
                //解析参数
                parameters[i] = parseValue(mapping, request, type);
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
    private Object parseValue(ParamMapping mapping, HttpRequest request, Class<?> type) {
        String alias = mapping.getAlisName();
        Object baseValue;
        Object value = null;
        switch (mapping.getParamType()) {
            case REQUEST_PARAM:
                value = convertValueType(request.getRequestParam().get(alias), type);
                break;
            case REQUEST_HEADER:
                value = convertValueType(request.getRequestHeader().get(alias), type);
                break;
            case REQUEST_CONTEXT:
                value = convertValueType(request.getRequestContext().get(alias), type);
                break;
            case REQUEST_ATTRIBUTE:
                value = convertValueType(request.getAttribute(alias), type);
                break;
            case REQUEST_FORM:

                if(type.isArray()) {
                    Class<?> componentType = type.getComponentType();

                    if(Convertible.convertible(componentType)) {
                        value = convertValueType(request.getRequestForm()
                                .getParams(alias).toArray(),componentType);
                    } else if(componentType.equals(MixedFileUpload.class)) {
                        value = request.getRequestForm()
                                .getUploads(alias).toArray(new MixedFileUpload[]{});
                    }

                } else if(Convertible.convertible(type)) {
                    value = convertValueType(request.getRequestForm().getParam(alias), type);
                } else if(type.equals(MixedFileUpload.class)) {
                    value = request.getRequestForm().getUpload(alias);
                }

                break;
            case REQUEST_BODY:
                baseValue = request.getRequestBody().getString();
                value = JsonTool.json2obj(baseValue.toString(), type);
                break;
            default:
                break;
        }

        return value;
    }

    public static void main(String[] args) {
        Integer[] array = new Integer[]{};

        if(array.getClass().isArray()) {
            Class<?> typeParameters = array.getClass().getComponentType();
            System.out.println(typeParameters);
        }
    }

}
