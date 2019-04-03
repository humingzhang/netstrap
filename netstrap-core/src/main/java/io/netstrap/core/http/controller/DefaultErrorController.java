package io.netstrap.core.http.controller;

import io.netstrap.core.http.stereotype.RestController;
import io.netstrap.core.http.stereotype.mapping.GetMapping;
import io.netstrap.core.http.stereotype.parameter.RequestAttribute;
import io.netstrap.core.http.stereotype.parameter.RequestContext;
import io.netstrap.core.http.ErrorPath;
import io.netstrap.core.http.HttpStatus;
import io.netstrap.core.http.datagram.HttpResponse;
import io.netstrap.core.http.wrapper.HttpBody;

/**
 * 异常控制器
 *
 * @author minghu.zhang
 * @date 2018/11/28 19:06
 */
@RestController
public class DefaultErrorController {

    @GetMapping(ErrorPath.BAD_REQUEST)
    public void badRequest(@RequestContext String uri, @RequestAttribute String message, HttpResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setBody(HttpBody.wrap((uri + ".Bad Request." + (message == null ? "" : message)).getBytes()));
    }

    @GetMapping(ErrorPath.FORBIDDEN)
    public void forbidden(@RequestContext String uri, @RequestAttribute String message, HttpResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN);
        response.setBody(HttpBody.wrap((uri + ".Forbidden." + (message == null ? "" : message)).getBytes()));
    }

    @GetMapping(ErrorPath.INTERNAL_SERVICE_ERROR)
    public void internalServiceError(@RequestContext String uri, @RequestAttribute String message, HttpResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setBody(HttpBody.wrap((uri + ".Internal Service Error." + (message == null ? "" : message)).getBytes()));
    }

    @GetMapping(ErrorPath.METHOD_NOT_ALLOWED)
    public void methodNotAllowed(@RequestContext String uri, @RequestAttribute String message, HttpResponse response) {
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
        response.setBody(HttpBody.wrap((uri + ".Method Not Allowed." + (message == null ? "" : message)).getBytes()));
    }

    @GetMapping(ErrorPath.NOT_FOUND)
    public void notFound(@RequestContext String uri, @RequestAttribute String message, HttpResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody(HttpBody.wrap((uri + ".Not Found." + (message == null ? "" : message)).getBytes()));
    }

    @GetMapping(ErrorPath.UNAUTHORIZED)
    public void unauthorized(@RequestContext String uri, @RequestAttribute String message, HttpResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED);
        response.setBody(HttpBody.wrap((uri + ".Unauthorized." + (message == null ? "" : message)).getBytes()));
    }

}
