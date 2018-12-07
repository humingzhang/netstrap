package io.netstrap.core.server.mvc.controller;

import io.netstrap.core.server.http.HttpStatus;
import io.netstrap.core.server.http.datagram.HttpResponse;
import io.netstrap.core.server.http.wrapper.HttpBody;
import io.netstrap.core.server.mvc.stereotype.RestController;
import io.netstrap.core.server.mvc.stereotype.mapping.GetMapping;
import io.netstrap.core.server.mvc.stereotype.parameter.AttributeValue;
import io.netstrap.core.server.mvc.stereotype.parameter.ContextValue;

/**
 * 异常控制器
 *
 * @author minghu.zhang
 * @date 2018/11/28 19:06
 */
@RestController
public class DefaultErrorController {

    /**
     * 400
     * 请求参数错误或不能解析
     */
    public static final String BAD_REQUEST = "/ERROR/BAD_REQUEST";

    /**
     * 401
     * 未获授权
     */
    public static final String UNAUTHORIZED = "/ERROR/UNAUTHORIZED";

    /**
     * 403
     * 禁止访问
     */
    public static final String FORBIDDEN = "/ERROR/FORBIDDEN";

    /**
     * 404
     * 路由找不到
     */
    public static final String NOT_FOUND = "/ERROR/NOT_FOUND";

    /**
     * 405
     * HTTP方法不支持
     */
    public static final String METHOD_NOT_ALLOWED = "/ERROR/METHOD_NOT_ALLOWED";

    /**
     * 500
     * 服务器内部错误
     */
    public static final String INTERNAL_SERVICE_ERROR = "/ERROR/INTERNAL_SERVICE_ERROR";

    @GetMapping(BAD_REQUEST)
    public void badRequest(@ContextValue String uri, @AttributeValue String message, HttpResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setBody(HttpBody.wrap((uri + ".Bad Request." + (message == null ? "" : message)).getBytes()));
    }

    @GetMapping(FORBIDDEN)
    public void forbidden(@ContextValue String uri, @AttributeValue String message, HttpResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN);
        response.setBody(HttpBody.wrap((uri + ".Forbidden." + (message == null ? "" : message)).getBytes()));
    }

    @GetMapping(INTERNAL_SERVICE_ERROR)
    public void internalServiceError(@ContextValue String uri, @AttributeValue String message, HttpResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setBody(HttpBody.wrap((uri + ".Internal Service Error." + (message == null ? "" : message)).getBytes()));
    }

    @GetMapping(METHOD_NOT_ALLOWED)
    public void methodNotAllowed(@ContextValue String uri, @AttributeValue String message, HttpResponse response) {
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
        response.setBody(HttpBody.wrap((uri + ".Method Not Allowed." + (message == null ? "" : message)).getBytes()));
    }

    @GetMapping(NOT_FOUND)
    public void notFound(@ContextValue String uri, @AttributeValue String message, HttpResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody(HttpBody.wrap((uri + ".Not Found." + (message == null ? "" : message)).getBytes()));
    }

    @GetMapping(UNAUTHORIZED)
    public void unauthorized(@ContextValue String uri, @AttributeValue String message, HttpResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED);
        response.setBody(HttpBody.wrap((uri + ".Unauthorized." + (message == null ? "" : message)).getBytes()));
    }

}
