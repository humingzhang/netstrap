package io.netstrap.core.server.netty.handler;

import io.netstrap.core.server.mvc.Dispatcher;
import io.netstrap.core.server.http.datagram.HttpRequest;
import io.netstrap.core.server.http.datagram.HttpResponse;
import io.netstrap.core.server.netty.datagram.NettyHttpRequest;
import io.netstrap.core.server.netty.datagram.NettyHttpResponse;
import io.netty.channel.*;
import io.netty.channel.ChannelHandler.Sharable;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.util.concurrent.EventExecutor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

/**
 * HTTP报文解析
 *
 * @author minghu.zhang
 * @date 2018/11/07
 */
@Component
@Sharable
@Log4j2
public class DefaultHttpHandler extends ChannelInboundHandlerAdapter {

    /**
     * 请求分发
     */
    private Dispatcher dispatcher;

    /**
     * 构造函数
     */
    @Autowired
    public DefaultHttpHandler(Dispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    @Override
    public void channelRead(ChannelHandlerContext context, Object msg) {
        FullHttpRequest req = (FullHttpRequest) msg;
        handleHttpRequest(context, req);
    }

    /**
     * 解析http请求
     */
    private void handleHttpRequest(ChannelHandlerContext context, FullHttpRequest req) {
        //创建请求对象
        HttpRequest request = new NettyHttpRequest(context, req)
                .parseContext()
                .parseMethod()
                .parseHeader()
                .parseParam()
                .parseBody();
        //创建响应对象
        HttpResponse response = new NettyHttpResponse(context.channel())
                .keepAlive(req.protocolVersion(), request.getRequestHeader());

        context.channel().eventLoop().execute(() -> {
            try {
                dispatcher.doDispatcher(request, response);
            } catch (Exception e) {
                exceptionCaught(context, e.getCause());
            }
        });
    }

    /**
     * 异常处理
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext context, Throwable cause) {
        context.channel().close();
    }
}
