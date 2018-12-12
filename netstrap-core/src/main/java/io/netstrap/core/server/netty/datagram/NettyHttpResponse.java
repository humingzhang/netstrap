package io.netstrap.core.server.netty.datagram;

import io.netstrap.core.server.http.Keepalive;
import io.netstrap.core.server.http.datagram.HttpResponse;
import static io.netstrap.core.server.http.header.HeaderPublicKey.*;

import io.netstrap.core.server.http.header.HeaderPublicKey;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.handler.codec.http.*;

import java.util.Map;
import java.util.Objects;

/**
 * Netty HTTP响应体
 *
 * @author minghu.zhang
 * @date 2018/11/08
 */
public class NettyHttpResponse extends HttpResponse {

    /**
     * channel数据管道
     */
    private Channel channel;
    /**
     * 响应对象
     */
    private FullHttpResponse response;

    /**
     * 构造函数
     */
    public NettyHttpResponse(Channel channel) {
        super();
        this.channel = channel;
    }

    @Override
    public void write() {
        if (isWritable()) {
            createResponse();
            addHeaders();
            writeFlush();
        } else {
            throw new RuntimeException("Response data can not be repeated output. ");
        }
    }

    /**
     * 创建响应对象
     */
    private void createResponse() {
        HttpResponseStatus status = HttpResponseStatus.valueOf(getStatus());
        if (Objects.nonNull(getBody())) {
            ByteBuf buffer = Unpooled.wrappedBuffer(getBody().getBytes());
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status, buffer);
        } else {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
        }
    }

    /**
     * 添加头信息
     */
    private void addHeaders() {
        for (String key : getHeader().keySet()) {
            response.headers().add(key, getHeader().get(key));
        }
    }

    /**
     * 写数据
     */
    private void writeFlush() {
        long length = getBody().getBytes().length;
        response.headers().add(CONTENT_LENGTH,length);
        ChannelFuture future = channel.writeAndFlush(response);

        if (!isKeepAlive()) {
            future.addListener(ChannelFutureListener.CLOSE);
        }

        setWritable(false);
    }


    /**
     * 设置keep-alive
     */
    public HttpResponse keepAlive(HttpVersion httpVersion, Map<String, String> header) {

        String connection = header.getOrDefault(HeaderPublicKey.CONNECTION, Keepalive.CLOSE_ALIVE).toLowerCase();
        //设置keep-alive
        if ((httpVersion.equals(HttpVersion.HTTP_1_1) && !connection.equals(Keepalive.CLOSE_ALIVE))) {
            setKeepAlive(true);
        } else if (httpVersion.equals(HttpVersion.HTTP_1_0) && connection.equals(Keepalive.KEEP_ALIVE)) {
            setKeepAlive(true);
        }

        if (isKeepAlive()) {
            addHeader(HeaderPublicKey.CONNECTION, Keepalive.KEEP_ALIVE);
        }

        return this;
    }

}
