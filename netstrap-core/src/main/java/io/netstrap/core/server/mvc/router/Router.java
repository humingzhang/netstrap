package io.netstrap.core.server.mvc.router;

/**
 * @author minghu.zhang
 * @Description TODO
 * @date 2018/12/5 11:36
 */

import io.netstrap.core.server.http.HttpMethod;
import lombok.Data;

import java.lang.reflect.Method;

/**
 * 路由数据模型
 * @author minghu.zhang
 * @date 2018/12/05
 */
@Data
public class Router {
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
