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
     * 请求方法
     */
    private HttpMethod method;

    /**
     * 附加参数（通常是Filter设置）
     */
    private Map<String, String> requestAttribute;

    /**
     * 请求上下文参数
     */
    private Map<String, String> requestContext;

    /**
     * 请求头
     */
    private Map<String, String> requestHeader;

    /**
     * 请求链接参数
     */
    private Map<String, String> requestParam;

    /**
     * 请求体
     */
    private HttpBody requestBody;

    /**
     * 表单对象
     */
    private HttpForm requestForm;

    /**
     * 获取请求方法
     */
    public HttpMethod getMethod() {
        return method;
    }

    /**
     * 获取上下文参数
     */
    public Map<String, String> getRequestContext() {
        return requestContext;
    }

    /**
     * 获取请求头
     */
    public Map<String, String> getRequestHeader() {
        return requestHeader;
    }

    /**
     * 获取链接参数
     */
    public Map<String, String> getRequestParam() {
        return requestParam;
    }

    /**
     * 获取请求体
     */
    public HttpBody getRequestBody() {
        return requestBody;
    }

    /**
     * 获取表单参数
     */
    public HttpForm getRequestForm() {
        return requestForm;
    }

    /**
     * 解析上下文参数
     *
     * @return this;
     */
    public abstract HttpRequest parseContext();

    /**
     * 解析Header
     *
     * @return this;
     */
    public abstract HttpRequest parseHeader();

    /**
     * 解析Body
     *
     * @return this;
     */
    public abstract HttpRequest parseBody();

    /**
     * 解析请求参数
     *
     * @return this;
     */
    public abstract HttpRequest parseParam();

    /**
     * 解析请求方法
     *
     * @return this;
     */
    public abstract HttpRequest parseMethod();

    /**
     * 释放内存
     *
     * @return this;
     */
    public abstract HttpRequest release();

}
