package io.netstrap.core.server.netty.handler;

import io.netstrap.core.server.config.SslConfig;
import io.netstrap.core.server.netty.NettyConfig;
import io.netstrap.core.server.websocket.AbstractStringDecoder;
import io.netstrap.core.server.websocket.decoder.DefaultStringDecoder;
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

import java.io.IOException;

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

    @Autowired
    public DefaultWebSocketHandler(DefaultHttpHandler httpHandler, NettyConfig nettyConfig) {
        this.httpHandler = httpHandler;
        this.nettyConfig = nettyConfig;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //连接
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        //断开
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        // http：//xxxx
        if (msg instanceof FullHttpRequest) {
            handleHttpRequest(ctx, (FullHttpRequest) msg);
        } else if (msg instanceof WebSocketFrame) {
            // ws://xxxx
            handlerWebSocketFrame(ctx, (WebSocketFrame) msg);
        }
    }

    /**
     * WebSocket消息处理
     *
     * @param ctx   管道上下文
     * @param frame WebSocket消息
     */
    private void handlerWebSocketFrame(ChannelHandlerContext context, WebSocketFrame frame) throws IOException {

        // 关闭请求
        if (frame instanceof CloseWebSocketFrame) {
            handshake.close(context.channel(), (CloseWebSocketFrame) frame.retain());
            return;
        }

        // ping请求
        if (frame instanceof PingWebSocketFrame) {
            context.channel().write(new PongWebSocketFrame(frame.content().retain()));
            return;
        }

        if (frame instanceof TextWebSocketFrame) {
            // 文本消息
            String text = ((TextWebSocketFrame) frame).text();
            AbstractStringDecoder decoder =
                    new DefaultStringDecoder(text).decode();
            context.channel().eventLoop().execute(() -> {
                try {
                    /*dispatcher.doDispatcher(request, response);*/
                } catch (Exception e) {
                    exceptionCaught(context, e.getCause());
                }
            });
        }
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

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        cause.printStackTrace();
        context.close();
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
