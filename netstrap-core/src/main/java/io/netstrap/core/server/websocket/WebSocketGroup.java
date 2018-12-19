package io.netstrap.core.server.websocket;

import io.netstrap.common.tool.Convertible;
import io.netstrap.common.tool.JsonTool;
import io.netty.channel.Channel;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Objects;

/**
 * WebSocket连接线程组
 * @author minghu.zhang
 * @date 2018/12/19 16:59
 */
public class WebSocketGroup {

    /**
     * 保存该服务器所有连接
     */
    private static final ChannelGroup ALL = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    /**
     * 登入
     */
    public static void login(Channel channel) {
        ALL.add(channel);
    }

    /**
     * 退出
     */
    public static void logout(Channel channel) {
        channel.close();
        ALL.remove(channel);
    }

    /**
     * 全部推送
     * @param message 推送内容
     */
    public static void push(Object message) {
        if (Objects.nonNull(message)) {
            TextWebSocketFrame tws;
            if(Convertible.convertible(message.getClass())) {
                tws = new TextWebSocketFrame(message.toString());
            } else {
                tws = new TextWebSocketFrame(JsonTool.obj2json(message));
            }
            ALL.writeAndFlush(tws);
        }
    }

    /**
     * 获取当前连接数
     * @return 连接数
     */
    public static int count() {
        return ALL.size();
    }

}
