package io.netstrap.core.server.websocket.dispatcher;

import io.netstrap.common.tool.Convertible;
import io.netstrap.common.tool.JsonTool;
import io.netstrap.core.server.websocket.AbstractStringDecoder;
import io.netstrap.core.server.websocket.WebSocketRouterFactory;
import io.netstrap.core.server.websocket.router.WebSocketAction;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 请求分发
 *
 * @author minghu.zhang
 * @date 2018/11/08
 */
@Component
public class WebSocketDispatcher {

    private final WebSocketRouterFactory factory;

    @Autowired
    public WebSocketDispatcher(WebSocketRouterFactory factory) {
        this.factory = factory;
    }

    /**
     * 请求分发
     */
    public void dispatcher(Channel channel, AbstractStringDecoder decoder) {
        //执行过滤分发
        try {
            WebSocketAction action = factory.get(decoder.uri());
            if (Objects.nonNull(action)) {
                Object[] params = getParams(action, channel, decoder.body());
                Object message = action.getAction().invoke(action.getInvoker(), params);
                if (Objects.nonNull(message)) {
                    TextWebSocketFrame tws;
                    if(Convertible.convertible(message.getClass())) {
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
    private Object[] getParams(WebSocketAction action, Channel channel, String body) {
        Class<?>[] types = action.getParamTypes();
        Object[] params = new Object[types.length];

        for (int i = 0; i < params.length; i++) {
            Class<?> type = types[i];
            if (type.equals(Channel.class)) {
                params[i] = channel;
            } else if (type.equals(String.class)) {
                params[i] = body;
            } else {
                params[i] = JsonTool.json2obj(body, type);
            }
        }
        return params;
    }
}
