package io.netstrap.core.server.websocket.dispatcher;

import io.netstrap.common.tool.Convertible;
import io.netstrap.common.tool.JsonTool;
import io.netstrap.core.server.websocket.AbstractStringDecoder;
import io.netstrap.core.server.websocket.WebSocketContext;
import io.netstrap.core.server.websocket.WebSocketDispatcher;
import io.netstrap.core.server.websocket.router.WebSocketRouterFactory;
import io.netstrap.core.server.websocket.decoder.DefaultStringDecoder;
import io.netstrap.core.server.websocket.router.WebSocketAction;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
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

    @Autowired
    public DefaultWebSocketDispatcher(WebSocketRouterFactory factory) {
        this.factory = factory;
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
                    handler(channel, context, new DefaultStringDecoder(text).decode());
                } catch (IOException e) {
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
        } catch (IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取调用参数
     */
    private Object[] getParams(WebSocketAction action, WebSocketContext context, Channel channel, AbstractStringDecoder decoder) {
        Class<?>[] types = action.getParamTypes();
        Object[] params = new Object[types.length];

        for (int i = 0; i < params.length; i++) {
            Class<?> type = types[i];
            if (type.equals(Channel.class)) {
                params[i] = channel;
            } else if (type.equals(String.class)) {
                params[i] = decoder.body();
            } else if (type.isAssignableFrom(Map.class)) {
                params[i] = decoder.param();
            } else {
                params[i] = JsonTool.json2obj(decoder.body(), type);
            }
        }
        return params;
    }

    /**
     * 处理异常
     */
    private void exceptionCaught(Channel channel, Throwable cause) {
        cause.printStackTrace();
        channel.close();
    }
}
