package io.netstrap.core.server.mvc.stereotype;

import io.netstrap.core.server.http.HttpMethod;

import java.lang.annotation.*;

/**
 * 标识PATCH请求
 * @author minghu.zhang
 */
@Target(value={ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = HttpMethod.PUT)
public @interface PatchMapping {
    String value() default "";
    /**
     * METHOD
     */
    HttpMethod[] method() default {HttpMethod.PATCH};
}
