package io.netstrap.core.server.http.datagram;

import io.netstrap.core.server.http.HttpMethod;
import io.netstrap.core.server.http.wrapper.HttpBody;
import io.netstrap.core.server.http.wrapper.HttpForm;
import lombok.Data;

import java.util.Map;

/**
 * Http请求数据报文
 *
 * @author minghu.zhang
 * @date 2018/11/07
 */
@Data
public abstract class HttpRequest {

    /**
     * 远程地址
     */
    private String ip;
    /**
     * 请求方法
     */
    private HttpMethod method;

    /**
     * 请求URI
     */
    private String uri;
    /**
     * 请求头
     */
    private Map<String, String> header;
    /**
     * 请求链接参数
     */
    private Map<String, String> param;
    /**
     * 请求体
     */
    private HttpBody body;
    /**
     * 表单对象
     */
    private HttpForm form;

    /**
     * 获取调用IP
     */
    public String getIp() {
        return ip;
    }

    /**
     * 获取请求方法
     */
    public HttpMethod getMethod() {
        return method;
    }

    /**
     * 获取请求URI
     */
    public String getUri() {
        return uri;
    }

    /**
     * 获取请求头
     */
    public Map<String, String> getHeader() {
        return header;
    }

    /**
     * 获取链接参数
     */
    public Map<String, String> getParam() {
        return param;
    }

    /**
     * 获取请求体
     */
    public HttpBody getBody() {
        return body;
    }

    /**
     * 获取表单参数
     */
    public HttpForm getForm() {
        return form;
    }

    /**
     * 解析IP
     * @return this;
     */
    public abstract HttpRequest parseIp();

    /**
     * 解析URI
     * @return this;
     */
    public abstract HttpRequest parseUri();

    /**
     * 解析Header
     * @return this;
     */
    public abstract HttpRequest parseHeader();

    /**
     * 解析Body
     * @return this;
     */
    public abstract HttpRequest parseBody();

    /**
     * 解析请求参数
     * @return this;
     */
    public abstract HttpRequest parseParam();

    /**
     * 解析请求方法
     * @return this;
     */
    public abstract HttpRequest parseMethod();

    /**
     * 释放内存
     * @return this;
     */
    public abstract HttpRequest release();

}
