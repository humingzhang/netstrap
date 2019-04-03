package io.netstrap.test.filter;

import io.netstrap.core.context.stereotype.Filterable;
import io.netstrap.core.websocket.WebSocketContext;
import io.netstrap.core.websocket.WebSocketFilter;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * WebSocket过滤器
 *
 * @author minghu.zhang
 * @date 2018/12/28 10:16
 */
@Filterable
public class MyWebSocketFilter implements WebSocketFilter {

    @Override
    public boolean filter(Channel channel, WebSocketContext context, WebSocketFrame frame) {
        //系统默认处理了CloseFrame和PingFrame，文本消息基于文本协议，此处只需要处理二进制消息
        //所有的websocket消息将经过该过滤器处理，Filterable可以指定排序值
        channel.writeAndFlush(new TextWebSocketFrame("hello world"));
        //此处返回true则继续执行，返回False则不会继续执行后面的逻辑
        return false;
    }
}
