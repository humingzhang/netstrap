package io.netstrap.core.server.http;

import io.netstrap.core.server.http.datagram.HttpRequest;
import io.netstrap.core.server.http.datagram.HttpResponse;
import io.netstrap.core.server.http.filter.DefaultWebFilter;

/**
 * 请求分发
 *
 * @author minghu.zhang
 * @date 2018/11/08
 */
public abstract class AbstractDispatcher {

    /**
     * 请求过滤器
     */
    private DefaultWebFilter filter;

    /**
     * 构造函数注入
     */
    public AbstractDispatcher(DefaultWebFilter filter) {
        this.filter = filter;
    }

    /**
     * 请求分发
     *
     * @param request  请求消息体
     * @param response 响应消息体
     * @throws Exception 抛出反射异常
     */
    protected abstract void dispatcher(HttpRequest request, HttpResponse response) throws Exception;

    /**
     * 请求分发
     *
     * @param request  请求消息体
     * @param response 响应消息体
     */
    public void doDispatcher(HttpRequest request, HttpResponse response) throws Exception {
        //执行过滤分发
        try {
            if (filter.doBefore(request, response)) {
                dispatcher(request, response);
                filter.doAfter(request, response);
            }
        } finally {
            request.release();
            response.write();
        }
    }
}
