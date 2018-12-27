package io.netstrap.core.server.websocket.dispatcher;

import io.netstrap.common.tool.Convertible;
import io.netstrap.common.tool.JsonTool;
import io.netstrap.core.server.websocket.AbstractStringDecoder;
import io.netstrap.core.server.websocket.WebSocketContext;
import io.netstrap.core.server.websocket.WebSocketDispatcher;
import io.netstrap.core.server.websocket.filter.DefaultWebSocketFilter;
import io.netstrap.core.server.websocket.router.WebSocketContextType;
import io.netstrap.core.server.websocket.router.WebSocketParamMapping;
import io.netstrap.core.server.websocket.router.WebSocketRouterFactory;
import io.netstrap.core.server.websocket.decoder.DefaultStringDecoder;
import io.netstrap.core.server.websocket.router.WebSocketAction;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.ConvertUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
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
        if (frame instanceof TextWebSocketFrame) {
            // 文本消息
            String text = ((TextWebSocketFrame) frame).text();
            channel.eventLoop().execute(() -> {
                try {
                    AbstractStringDecoder decoder = new DefaultStringDecoder(text)
                            .decode();
                    if (webSocketFilter.filter(channel, context, decoder.body())) {
                        handler(channel, context, decoder);
                    }
                } catch (Exception e) {
                    exceptionCaught(channel, e.getCause());
                }
            });
        }
    }

    /**
     * 请求分发
     */
    private void handler(Channel channel, WebSocketContext context, AbstractStringDecoder decoder) {
        //执行过滤分发
        try {
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
            e.printStackTrace();
        }

    }

    /**
     * 获取调用参数
     */
    private Object[] getParams(WebSocketAction action, WebSocketContext context, Channel channel, AbstractStringDecoder decoder) throws IllegalAccessException, InstantiationException, InvocationTargetException {
        WebSocketParamMapping[] mappings = action.getMappings();
        Object[] params = new Object[mappings.length];

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

                Object value;
                Class<?> paramClass = mapping.getParamClass();
                switch (contextType) {
                    case REQUEST_PARAM:
                        value = getContextValue(mapping, paramClass, param);
                        break;
                    case REQUEST_BODY:
                        value = JsonTool.json2obj(decoder.body(), mapping.getParamClass());
                        break;
                    case REQUEST_ATTRIBUTE:
                        value = getContextValue(mapping, paramClass, attribute);
                        break;
                    default: {
                        value = null;
                        break;
                    }
                }
                params[i] = value;
            }
        }

        return params;
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
