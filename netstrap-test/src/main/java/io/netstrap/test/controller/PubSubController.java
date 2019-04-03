package io.netstrap.test.controller;

import io.netstrap.core.websocket.stereotype.mapping.WebSocketController;
import io.netstrap.core.websocket.stereotype.mapping.WebSocketMapping;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import lombok.extern.log4j.Log4j2;

/**
 * 控制器示例
 *
 * @author minghu.zhang
 * @date 2018/11/29 11:01
 */
@WebSocketController
@Log4j2
public class PubSubController {


    /**
     * 发布主题
     *
     * @param channel 管道
     * @param body    消息体
     */
    @WebSocketMapping("/pub/subject")
    public void pubSubject(Channel channel, String body) {
        log.info(body);
        channel.writeAndFlush(new TextWebSocketFrame(body));
    }

}
