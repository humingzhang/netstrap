package io.netstrap.core.server.mvc.stereotype;

import io.netstrap.core.server.http.HttpMethod;

import java.lang.annotation.*;

/**
 * 标识DELETE请求
 * @author minghu.zhang
 */
@Target(value={ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DeleteMapping {
    String value();
    /**
     * METHOD
     */
    HttpMethod[] method() default {HttpMethod.DELETE};
}
