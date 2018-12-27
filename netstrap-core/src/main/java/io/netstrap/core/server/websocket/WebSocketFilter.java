package io.netstrap.core.server.websocket;

import io.netty.channel.Channel;

/**
 * 过滤器接口
 *
 * @author minghu.zhang
 * @date 2018/11/08
 */
public interface WebSocketFilter {

    /**
     * 过滤器
     * @param channel 链接通道
     * @param context 上下文
     * @param body    文本报文
     * @return 执行结果，是否需要继续执行
     * @throws Exception 解析异常
     */
    boolean filter(Channel channel,WebSocketContext context,String body) throws Exception;

}
