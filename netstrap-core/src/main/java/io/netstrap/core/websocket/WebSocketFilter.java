package io.netstrap.core.websocket;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * 过滤器接口
 *
 * @author minghu.zhang
 * @date 2018/11/08
 */
public interface WebSocketFilter {

    /**
     * 过滤器
     *
     * @param channel 链接通道
     * @param context 上下文
     * @param frame   请求报文
     * @return 执行结果，是否需要继续执行
     */
    boolean filter(Channel channel, WebSocketContext context, WebSocketFrame frame);

}
