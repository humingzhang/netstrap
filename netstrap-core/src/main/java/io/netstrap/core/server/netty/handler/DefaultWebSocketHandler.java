package io.netstrap.core.server.netty.handler;

import io.netstrap.core.server.config.SslConfig;
import io.netstrap.core.server.netty.NettyConfig;
import io.netstrap.core.server.websocket.ChannelInactiveRunListener;
import io.netstrap.core.server.websocket.dispatcher.WebSocketDispatcher;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.HttpHeaderNames;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * WebSocket解析
 *
 * @author minghu.zhang
 * @date 2018/12/18
 */
@Component
@Sharable
@Log4j2
public class DefaultWebSocketHandler extends SimpleChannelInboundHandler<Object> {
    /**
     * 握手处理
     */
    private WebSocketServerHandshaker handshake;
    /**
     * http处理
     */
    private final DefaultHttpHandler httpHandler;
    /**
     * netty配置
     */
    private final NettyConfig nettyConfig;
    /**
     * socket处理
     */
    private final WebSocketDispatcher dispatcher;
    /**
     * inactive 监听器
     */
    private final ChannelInactiveRunListener listener;

    @Autowired
    public DefaultWebSocketHandler(DefaultHttpHandler httpHandler, NettyConfig nettyConfig,
                                   WebSocketDispatcher dispatcher, ChannelInactiveRunListener listener) {
        this.httpHandler = httpHandler;
        this.nettyConfig = nettyConfig;
        this.dispatcher = dispatcher;
        this.listener = listener;
    }

    @Override
    public void channelActive(ChannelHandlerContext context) {
        //连接
        listener.channelActive(context.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext context) {
        //断开
        listener.channelInactive(context.channel());
    }

    @Override
    protected void channelRead0(ChannelHandlerContext context, Object msg) throws Exception {
        if (msg instanceof FullHttpRequest) {
            // http//xxxx
            handleHttpRequest(context, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            // ws://xxxx
            handleWebSocketFrame(context, (WebSocketFrame) msg);
        }
    }

    /**
     * WebSocket消息处理
     *
     * @param context 管道上下文
     * @param frame   WebSocket消息
     */
    private void handleWebSocketFrame(ChannelHandlerContext context, WebSocketFrame frame) {
        Channel channel = context.channel();
        // 关闭请求
        if (frame instanceof CloseWebSocketFrame) {
            handshake.close(channel, (CloseWebSocketFrame) frame.retain());
            return;
        }

        // ping请求
        if (frame instanceof PingWebSocketFrame) {
            channel.write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        dispatcher.dispatcher(channel, frame);
    }

    /**
     * 处理Http请求或握手请求
     */
    private void handleHttpRequest(ChannelHandlerContext context, FullHttpRequest request) {

        if (!request.decoderResult().isSuccess()) {
            context.close();
            return;
        }

        if (nettyConfig.getEndpoint().equals(request.uri())) {
            //建立WebSocket连接
            SslConfig ssl = nettyConfig.getSsl();
            WebSocketServerHandshakerFactory wsFactory =
                    new WebSocketServerHandshakerFactory(ssl.isEnable() ? "wss://" : "ws://" + request.headers().get(HttpHeaderNames.HOST) + request.uri(), null, false);

            handshake = wsFactory.newHandshaker(request);
            if (handshake == null) {
                // 不支持
                WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(context.channel());
            } else {
                handshake.handshake(context.channel(), request);
            }
        } else {
            // 处理普通http请求
            httpHandler.handleHttpRequest(context, request);
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext context, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {

            IdleStateEvent event = (IdleStateEvent) evt;
            if (event.state() == IdleState.READER_IDLE) {
                // 读数据超时
            } else if (event.state() == IdleState.WRITER_IDLE) {
                // 写数据超时
            } else if (event.state() == IdleState.ALL_IDLE) {
                // 通道长时间没有读写，服务端主动断开链接
                context.close();
            }

        } else {
            super.userEventTriggered(context, evt);
        }
    }

}
