package io.netstrap.core.server.websocket;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * WebSocket分发接口
 * @author minghu.zhang
 * @date 2018/12/24 10:58
 */
public interface WebSocketDispatcher {

    /**
     * 请求分发
     * @param channel 连接管道
     * @param frame   数据帧
     */
    void dispatcher(Channel channel, WebSocketFrame frame);

}
