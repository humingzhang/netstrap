package io.netstrap.core.server.mvc.stereotype;

import io.netstrap.core.server.http.HttpMethod;

import java.lang.annotation.*;

/**
 * 标识GET请求
 * @author minghu.zhang
 */
@Target(value={ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = HttpMethod.GET)
public @interface GetMapping {
    String value();
    /**
     * METHOD
     */
    HttpMethod[] method() default {HttpMethod.GET};
}
