package io.netstrap.core.server.http.datagram;

import io.netstrap.core.server.http.wrapper.HttpBody;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;

import static io.netstrap.core.server.http.header.HeaderPublicKey.CONTENT_LENGTH;

/**
 * Http响应数据报文
 *
 * @author minghu.zhang
 * @date 2018/11/07
 */
@Data
public abstract class HttpResponse {

    /**
     * 请求状态码
     */
    private int status = 200;

    /**
     * 是否保持连接
     */
    private boolean keepAlive;

    /**
     * 响应头信息
     */
    private Map<String, String> header = new HashMap<>(8);

    /**
     * 响应体数据
     */
    private HttpBody body;
    /**
     * 是否可写
     */
    private boolean writable = true;

    /**
     * 添加响应头
     */
    public void addHeader(String key, Object value) {
        this.header.put(key, value.toString());
    }

    /**
     * 添加响应体
     *
     * @return this
     */
    public HttpResponse setBody(HttpBody body) {
        this.body = body;
        return this;
    }

    /**
     * 写入数据
     */
    public abstract void write();

    /**
     * 查询通道是否可写
     */
    public boolean isWritable() {
        return writable;
    }
}
