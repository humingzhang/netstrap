package io.netstrap.core.server.websocket;

import io.netty.channel.Channel;
import lombok.Data;

/**
 * WebSocketContext
 * @author minghu.zhang
 * @date 2018/12/27 19:30
 */
@Data
public class WebSocketContext {

    /**
     * 请求ID
     */
    private String id;
    /**
     * 客户端IP
     */
    private String ip;
    /**
     * 连接管道
     */
    private Channel channel;

}
