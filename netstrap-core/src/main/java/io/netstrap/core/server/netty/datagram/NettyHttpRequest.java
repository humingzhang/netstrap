package io.netstrap.core.server.netty.datagram;


import io.netstrap.core.server.http.header.HeaderKey;
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
import org.springframework.util.StringUtils;

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

    @Override
    public HttpRequest parseIp() {
        setIp(context.channel().remoteAddress().toString().replaceAll("/", "")
                .split(":")[0]);
        return this;
    }

    @Override
    public HttpRequest parseUri() {
        if (StringUtils.isEmpty(getUri())) {
            String uri = request.uri();
            String split = "?";
            if (uri.contains(split)) {
                uri = uri.substring(0, request.uri().indexOf(split));
            }

            try {
                setUri(URLDecoder.decode(uri, "utf-8"));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    @Override
    public HttpRequest parseHeader() {
        if (Objects.isNull(getHeader())) {
            Map<String, String> headers = new HashMap<>(8);
            List<Map.Entry<String, String>> entryList = request.headers().entries();

            for (Map.Entry<String, String> entry : entryList) {
                String key = entry.getKey();
                headers.put(key, entry.getValue());
            }
            setHeader(headers);
        }
        return this;
    }

    @Override
    public HttpRequest parseParam() {
        if (Objects.isNull(getParam())) {
            Map<String, String> httpParams = new HashMap<>(8);
            Map<String, List<String>> queryParams = new QueryStringDecoder(request.uri())
                    .parameters();

            for (Map.Entry<String, List<String>> entry : queryParams.entrySet()) {
                httpParams.put(entry.getKey(), entry.getValue().get(0));
            }
            setParam(httpParams);
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
        if(request.refCnt() > 0) {
            request.release();
        }
        return this;
    }

    @Override
    public HttpRequest parseBody() {
        if (Objects.isNull(getBody()) || Objects.isNull(getForm())) {
            //POST请求需要解析请求体
            if (POST.equals(getMethod())) {
                String type = getHeader().get(HeaderKey.CONTENT_TYPE);
                try {
                    boolean isForm = type.contains("form");
                    if (isForm) {
                        decodeForm();
                    } else {
                        decodeBody();
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
    private void decodeBody() {
        if (Objects.isNull(getBody())) {
            ByteBuf content = request.content();
            byte[] body = new byte[content.readableBytes()];
            content.getBytes(0, body);
            content.release();
            setBody(HttpBody.wrap(body));
        }
    }

    /**
     * 解析Form
     */
    private void decodeForm() throws IOException {
        if (Objects.isNull(getForm())) {
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
            setForm(form);
        }
    }

}
