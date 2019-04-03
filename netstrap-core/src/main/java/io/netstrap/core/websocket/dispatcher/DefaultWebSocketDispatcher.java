package io.netstrap.core.websocket.dispatcher;

import io.netstrap.common.tool.Convertible;
import io.netstrap.common.tool.JsonTool;
import io.netstrap.core.exception.ParameterParseException;
import io.netstrap.core.websocket.AbstractStringDecoder;
import io.netstrap.core.websocket.WebSocketContext;
import io.netstrap.core.websocket.WebSocketDispatcher;
import io.netstrap.core.websocket.filter.DefaultWebSocketFilter;
import io.netstrap.core.websocket.router.WebSocketContextType;
import io.netstrap.core.websocket.router.WebSocketParamMapping;
import io.netstrap.core.websocket.router.WebSocketRouterFactory;
import io.netstrap.core.websocket.decoder.DefaultStringDecoder;
import io.netstrap.core.websocket.router.WebSocketAction;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.Objects;

/**
 * 请求分发
 *
 * @author minghu.zhang
 * @date 2018/11/08
 */
@Component
public class DefaultWebSocketDispatcher implements WebSocketDispatcher {

    private final WebSocketRouterFactory factory;
    private final DefaultWebSocketFilter webSocketFilter;

    @Autowired
    public DefaultWebSocketDispatcher(WebSocketRouterFactory factory, DefaultWebSocketFilter webSocketFilter) {
        this.factory = factory;
        this.webSocketFilter = webSocketFilter;
    }

    /**
     * 请求分发
     */
    @Override
    public void dispatcher(Channel channel, WebSocketContext context, WebSocketFrame frame) {
        if (webSocketFilter.filter(channel, context, frame)) {
            if (frame instanceof TextWebSocketFrame) {
                // 文本消息
                String text = ((TextWebSocketFrame) frame).text();
                handler(channel, context, text);
            }
        }
        //释放内存
        frame.release();
    }

    /**
     * 请求分发
     */
    private void handler(Channel channel, WebSocketContext context, String text) {
        //执行过滤分发
        try {
            AbstractStringDecoder decoder = new DefaultStringDecoder(text).decode();
            WebSocketAction action = factory.get(decoder.uri());
            if (Objects.nonNull(action)) {
                Object[] params = getParams(action, context, channel, decoder);
                Object message = action.getAction().invoke(action.getInvoker(), params);
                if (Objects.nonNull(message)) {
                    TextWebSocketFrame tws;
                    if (Convertible.convertible(message.getClass())) {
                        tws = new TextWebSocketFrame(message.toString());
                    } else {
                        tws = new TextWebSocketFrame(JsonTool.obj2json(message));
                    }
                    channel.writeAndFlush(tws);
                }
            }
        } catch (Exception e) {
            exceptionCaught(channel, e);
        }

    }

    /**
     * 获取调用参数
     */
    private Object[] getParams(WebSocketAction action, WebSocketContext context, Channel channel, AbstractStringDecoder decoder) {
        WebSocketParamMapping[] mappings = action.getMappings();
        Object[] params = new Object[mappings.length];

        try {
            for (int i = 0; i < mappings.length; i++) {
                WebSocketParamMapping mapping = mappings[i];
                if (mapping.getParamClass().equals(Channel.class)) {
                    params[i] = channel;
                } else if (mapping.getParamClass().equals(WebSocketContext.class)) {
                    params[i] = context;
                } else {
                    WebSocketContextType contextType = mapping.getContextType();
                    Map<String, String> param = decoder.param();
                    Map<String, ?> attribute = context.getAttribute();
                    params[i] = getObject(decoder, mapping, contextType, param, attribute);
                }
            }
        } catch (Exception e) {
            throw new ParameterParseException("Parameter transformation exception.");
        }

        return params;
    }

    /**
     * 获取参数值
     */
    private Object getObject(AbstractStringDecoder decoder,
                             WebSocketParamMapping mapping,
                             WebSocketContextType contextType,
                             Map<String, String> param,
                             Map<String, ?> attribute) throws Exception {
        Object value = null;

        Class<?> paramClass = mapping.getParamClass();
        switch (contextType) {
            case REQUEST_PARAM:
                value = getContextValue(mapping, paramClass, param);
                break;
            case REQUEST_BODY:
                if (paramClass.equals(String.class)) {
                    value = decoder.body();
                } else {
                    value = JsonTool.json2obj(decoder.body(), mapping.getParamClass());
                }
                break;
            case REQUEST_ATTRIBUTE:
                value = getContextValue(mapping, paramClass, attribute);
                break;
            default: {
                break;
            }
        }

        return value;
    }

    /**
     * 获取参数值
     */
    private Object getContextValue(WebSocketParamMapping mapping, Class<?> paramClass, Map<String, ?> paramContext) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        Object value;

        if (Convertible.convertible(mapping.getParamClass())) {
            //基本数据类型
            value = convertValueType(paramContext.get(mapping.getAlisName()), mapping.getParamClass());
        } else {
            value = paramClass.newInstance();
            BeanUtils.copyProperties(paramContext, value);
        }

        return value;
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
     * 处理异常
     */
    private void exceptionCaught(Channel channel, Throwable cause) {
        cause.printStackTrace();
        channel.close();
    }
}
