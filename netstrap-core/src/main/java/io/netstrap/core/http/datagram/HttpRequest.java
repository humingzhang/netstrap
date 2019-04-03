package io.netstrap.core.http.datagram;

import io.netstrap.core.http.router.HttpMethod;
import io.netstrap.core.http.wrapper.HttpBody;
import io.netstrap.core.http.wrapper.HttpForm;
import lombok.Data;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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
    private Map<String, Object> requestAttribute;

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
     * 设置参数
     */
    public void addAttribute(String key, Object value) {
        if (Objects.isNull(requestAttribute)) {
            requestAttribute = new HashMap<>(8);
        }
        requestAttribute.put(key, value);
    }

    /**
     * 获取参数
     */
    public <N> N getAttribute(String key) {
        if (Objects.isNull(requestAttribute)) {
            requestAttribute = new HashMap<>(8);
        }
        return (N) requestAttribute.get(key);
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

    protected HttpRequest setMethod(HttpMethod method) {
        this.method = method;
        return this;
    }

    protected HttpRequest setRequestAttribute(Map<String, Object> requestAttribute) {
        this.requestAttribute = requestAttribute;
        return this;
    }

    protected HttpRequest setRequestContext(Map<String, String> requestContext) {
        this.requestContext = requestContext;
        return this;
    }

    protected HttpRequest setRequestHeader(Map<String, String> requestHeader) {
        this.requestHeader = requestHeader;
        return this;
    }

    protected HttpRequest setRequestParam(Map<String, String> requestParam) {
        this.requestParam = requestParam;
        return this;
    }

    protected HttpRequest setRequestBody(HttpBody requestBody) {
        this.requestBody = requestBody;
        return this;
    }

    protected HttpRequest setRequestForm(HttpForm requestForm) {
        this.requestForm = requestForm;
        return this;
    }
}
