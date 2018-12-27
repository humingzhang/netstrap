package io.netstrap.core.server.http.controller;

import io.netstrap.core.server.http.DefaultErrorUri;
import io.netstrap.core.server.http.HttpStatus;
import io.netstrap.core.server.http.datagram.AbstractHttpResponse;
import io.netstrap.core.server.http.wrapper.HttpBody;
import io.netstrap.core.server.http.stereotype.RestController;
import io.netstrap.core.server.http.stereotype.mapping.GetMapping;
import io.netstrap.core.server.http.stereotype.parameter.RequestAttribute;
import io.netstrap.core.server.http.stereotype.parameter.RequestContext;

/**
 * 异常控制器
 *
 * @author minghu.zhang
 * @date 2018/11/28 19:06
 */
@RestController
public class DefaultErrorController {

    @GetMapping(DefaultErrorUri.BAD_REQUEST)
    public void badRequest(@RequestContext String uri, @RequestAttribute String message, AbstractHttpResponse response) {
        response.setStatus(HttpStatus.BAD_REQUEST);
        response.setBody(HttpBody.wrap((uri + ".Bad Request." + (message == null ? "" : message)).getBytes()));
    }

    @GetMapping(DefaultErrorUri.FORBIDDEN)
    public void forbidden(@RequestContext String uri, @RequestAttribute String message, AbstractHttpResponse response) {
        response.setStatus(HttpStatus.FORBIDDEN);
        response.setBody(HttpBody.wrap((uri + ".Forbidden." + (message == null ? "" : message)).getBytes()));
    }

    @GetMapping(DefaultErrorUri.INTERNAL_SERVICE_ERROR)
    public void internalServiceError(@RequestContext String uri, @RequestAttribute String message, AbstractHttpResponse response) {
        response.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        response.setBody(HttpBody.wrap((uri + ".Internal Service Error." + (message == null ? "" : message)).getBytes()));
    }

    @GetMapping(DefaultErrorUri.METHOD_NOT_ALLOWED)
    public void methodNotAllowed(@RequestContext String uri, @RequestAttribute String message, AbstractHttpResponse response) {
        response.setStatus(HttpStatus.METHOD_NOT_ALLOWED);
        response.setBody(HttpBody.wrap((uri + ".Method Not Allowed." + (message == null ? "" : message)).getBytes()));
    }

    @GetMapping(DefaultErrorUri.NOT_FOUND)
    public void notFound(@RequestContext String uri, @RequestAttribute String message, AbstractHttpResponse response) {
        response.setStatus(HttpStatus.NOT_FOUND);
        response.setBody(HttpBody.wrap((uri + ".Not Found." + (message == null ? "" : message)).getBytes()));
    }

    @GetMapping(DefaultErrorUri.UNAUTHORIZED)
    public void unauthorized(@RequestContext String uri, @RequestAttribute String message, AbstractHttpResponse response) {
        response.setStatus(HttpStatus.UNAUTHORIZED);
        response.setBody(HttpBody.wrap((uri + ".Unauthorized." + (message == null ? "" : message)).getBytes()));
    }

}
