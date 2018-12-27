package io.netstrap.core.server.http.router;


import lombok.Data;

import java.lang.reflect.Method;

/**
 * 调用模型
 *
 * @author minghu.zhang
 * @date 2018/12/05
 */
@Data
public class HttpAction {
    /**
     * 参数映射
     */
    private HttpParamMapping[] mappings;
    /**
     * 当前映射的URI
     */
    private String uri;
    /**
     * 调用对象
     */
    private Object invoker;
    /**
     * 调用方法
     */
    private Method action;
    /**
     * 支持的HTTP方法
     */
    private HttpMethod[] methods;
}
