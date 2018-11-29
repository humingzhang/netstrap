package io.netstrap.core.server.mvc.controller;

import io.netstrap.core.server.http.HttpStatus;
import io.netstrap.core.server.http.datagram.HttpRequest;
import io.netstrap.core.server.http.datagram.HttpResponse;
import io.netstrap.core.server.http.wrapper.HttpBody;
import io.netstrap.core.server.mvc.stereotype.RequestMapping;
import io.netstrap.core.server.mvc.stereotype.RestController;

/**
 * @Description 异常控制器
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

    @RequestMapping(value = BAD_REQUEST)
    public void badRequest(HttpRequest request, HttpResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setBody(HttpBody.wrap("Bad Request".getBytes()));
    }

    @RequestMapping(value = FORBIDDEN)
    public void forbidden(HttpRequest request, HttpResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN);
        response.setBody(HttpBody.wrap("Forbidden".getBytes()));
    }

    @RequestMapping(value = INTERNAL_SERVICE_ERROR)
    public void internalServiceError(HttpRequest request, HttpResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setBody(HttpBody.wrap("Internal Service Error".getBytes()));
    }

    @RequestMapping(value = METHOD_NOT_ALLOWED)
    public void methodNotAllowed(HttpRequest request, HttpResponse response) {
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
        response.setBody(HttpBody.wrap("Method Not Allowed".getBytes()));
    }

    @RequestMapping(value = NOT_FOUND)
    public void notFound(HttpRequest request, HttpResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody(HttpBody.wrap("Not Found".getBytes()));
    }

    @RequestMapping(value = UNAUTHORIZED)
    public void unauthorized(HttpRequest request, HttpResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED);
        response.setBody(HttpBody.wrap("Unauthorized".getBytes()));
    }

}
