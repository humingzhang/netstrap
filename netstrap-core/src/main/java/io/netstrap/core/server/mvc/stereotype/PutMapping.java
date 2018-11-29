package io.netstrap.core.server.mvc.stereotype;

import io.netstrap.core.server.http.HttpMethod;

import java.lang.annotation.*;

/**
 * 标识PUT请求
 * @author minghu.zhang
 */
@Target(value={ElementType.TYPE,ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = HttpMethod.PUT)
public @interface PutMapping {
    String value();
    /**
     * METHOD
     */
    HttpMethod[] method() default {HttpMethod.POST};
}
