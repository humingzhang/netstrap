package io.netstrap.core.server.netty.datagram;


import io.netstrap.common.encrypt.MD5;
import io.netstrap.core.server.http.header.HeaderPublicKey;
import io.netstrap.core.server.http.datagram.HttpRequest;
import io.netstrap.core.server.http.HttpMethod;
import io.netstrap.core.server.http.wrapper.HttpBody;
import io.netstrap.core.server.http.wrapper.HttpForm;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.QueryStringDecoder;
import io.netty.handler.codec.http.multipart.Attribute;
import io.netty.handler.codec.http.multipart.HttpPostRequestDecoder;
import io.netty.handler.codec.http.multipart.InterfaceHttpData;
import io.netty.handler.codec.http.multipart.MixedFileUpload;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static io.netstrap.core.server.http.HttpMethod.POST;

/**
 * Netty HTTP 消息对象
 *
 * @author minghu.zhang
 * @date 2018/11/07
 */
public class NettyHttpRequest extends HttpRequest {

    /**
     * 从Netty接收的请求报文
     */
    private FullHttpRequest request;
    /**
     * 连接通道
     */
    private ChannelHandlerContext context;

    /**
     * 构造函数
     */
    public NettyHttpRequest(ChannelHandlerContext context, FullHttpRequest request) {
        super();
        this.request = request;
        this.context = context;
    }

    /**
     * 获取远程IP
     */
    private String getRemoteIp() {
        return context.channel().remoteAddress().toString().replaceAll("/", "")
                .split(":")[0];
    }

    /**
     * 获取请求URI
     */
    public String getRequestUri() {
        String uri = request.uri();
        String split = "?";
        if (uri.contains(split)) {
            uri = uri.substring(0, request.uri().indexOf(split));
        }

        try {
            uri = URLDecoder.decode(uri, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        return uri;
    }

    /**
     * 获取请求ID
     */
    public String getRequestId() {
        return MD5.encrypt16(context.channel().id().asLongText());
    }

    @Override
    public HttpRequest parseContext() {

        if (Objects.isNull(getRequestContext())) {
            Map<String, String> context = new HashMap<>(3);

            context.put("ip", getRemoteIp());
            context.put("uri", getRequestUri());
            context.put("id", getRequestId());

            setRequestContext(context);
        }

        return this;
    }

    @Override
    public HttpRequest parseHeader() {
        if (Objects.isNull(getRequestHeader())) {
            Map<String, String> headers = new HashMap<>(8);
            List<Map.Entry<String, String>> entryList = request.headers().entries();

            for (Map.Entry<String, String> entry : entryList) {
                String key = entry.getKey();
                headers.put(key, entry.getValue());
            }
            setRequestHeader(headers);
        }
        return this;
    }

    @Override
    public HttpRequest parseParam() {
        if (Objects.isNull(getRequestParam())) {
            Map<String, String> httpParams = new HashMap<>(8);
            Map<String, List<String>> queryParams = new QueryStringDecoder(request.uri())
                    .parameters();

            for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
                httpParams.put(entry.getKey(), entry.getValue().get(0));
            }

            setRequestParam(httpParams);
        }
        return this;
    }

    @Override
    public HttpRequest parseMethod() {
        if (Objects.isNull(getMethod())) {
            String name = request.method().name().toUpperCase();

            for (HttpMethod httpMethod : HttpMethod.values()) {
                if (httpMethod.name().equals(name)) {
                    setMethod(httpMethod);
                    break;
                }
            }
        }
        return this;
    }

    @Override
    public HttpRequest release() {
        if (request.refCnt() > 0) {
            request.release();
        }
        return this;
    }

    @Override
    public HttpRequest parseBody() {
        if (Objects.isNull(getRequestBody()) || Objects.isNull(getRequestForm())) {
            //POST请求需要解析请求体
            if (POST.equals(getMethod())) {

                String type = getRequestHeader().getOrDefault(HeaderPublicKey.CONTENT_TYPE, "");
                try {
                    boolean isForm = type.contains("form");
                    if (isForm) {
                        decodeRequestForm();
                    } else {
                        decodeRequestBody();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return this;
    }

    /**
     * 解析Body
     */
    private void decodeRequestBody() {
        if (Objects.isNull(getRequestBody())) {
            ByteBuf content = request.content();
            byte[] body = new byte[content.readableBytes()];
            content.getBytes(0, body);
            content.release();
            setRequestBody(HttpBody.wrap(body));
        }
    }

    /**
     * 解析Form
     */
    private void decodeRequestForm() throws IOException {
        if (Objects.isNull(getRequestForm())) {
            HttpForm form = new HttpForm();
            HttpPostRequestDecoder decoder = new HttpPostRequestDecoder(request);
            List<InterfaceHttpData> inputs = decoder.getBodyHttpDatas();

            for (InterfaceHttpData input : inputs) {
                if (input instanceof MixedFileUpload) {
                    MixedFileUpload data = (MixedFileUpload) input;
                    form.upload(input.getName(), data);
                }

                if (input instanceof Attribute) {
                    Attribute data = (Attribute) input;
                    form.param(data.getName(), data.getValue());
                }
            }
            setRequestForm(form);
        }
    }

}
