package io.netstrap.core.server.websocket;

import io.netstrap.core.server.http.datagram.AbstractHttpRequest;
import io.netstrap.core.server.http.datagram.AbstractHttpResponse;
import io.netstrap.core.server.websocket.router.WebSocketAction;
import io.netty.channel.Channel;

/**
 * 过滤器接口
 *
 * @author minghu.zhang
 * @date 2018/11/08
 */
public interface WebSocketFilter {

    /**
     * 执行过滤
     * @throws Exception 异常抛出，交给统一异常处理器
     */
    void filter(Channel channel, WebSocketAction action,WebSocketContext context) throws Exception;

}
