package io.netstrap.core.websocket;

import io.netstrap.common.encrypt.MD5;
import io.netty.channel.Channel;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

/**
 * WebSocketContext
 *
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
     * 属性对象
     */
    private Map<String,?> attribute = new HashMap<>();

    /**
     * 解析Context
     */
    public WebSocketContext parseContext(Channel channel) {

        parseId(channel);
        parseIp(channel);

        return this;
    }

    /**
     * 获取远程IP
     */
    private void parseIp(Channel channel) {
        ip = channel.remoteAddress().toString().replaceAll("/", "")
                .split(":")[0];
    }

    private void parseId(Channel channel) {
        id = MD5.encrypt16(channel.id().asLongText());
    }

}
