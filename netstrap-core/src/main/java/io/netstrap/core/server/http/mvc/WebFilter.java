package io.netstrap.core.server.http.mvc;

import io.netstrap.core.server.http.datagram.AbstractHttpRequest;
import io.netstrap.core.server.http.datagram.AbstractHttpResponse;

/**
 * 过滤器接口
 *
 * @author minghu.zhang
 * @date 2018/11/08
 */
public interface WebFilter {

    /**
     * 执行之前，true 表示继续执行，false表示中断执行
     *
     * @param request  请求消息
     * @param response 响应消息
     * @return boolean 是否继续执行
     * @throws Exception 异常抛出，交给统一异常处理器
     */
    boolean doBefore(AbstractHttpRequest request, AbstractHttpResponse response) throws Exception;

    /**
     * 执行之后，true 表示继续执行，false表示结束
     *
     * @param request  请求消息
     * @param response 响应消息
     * @return boolean 是否继续执行
     * @throws Exception 异常抛出，交给统一异常处理器
     */
    boolean doAfter(AbstractHttpRequest request, AbstractHttpResponse response) throws Exception;
}
