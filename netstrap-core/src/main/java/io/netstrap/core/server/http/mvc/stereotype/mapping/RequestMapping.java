package io.netstrap.core.server.http.mvc.stereotype.mapping;

import io.netstrap.core.server.http.HttpMethod;

import java.lang.annotation.*;

/**
 * 标识REQUEST请求
 *
 * @author minghu.zhang
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestMapping {

    /**
     * URI
     */
    String value() default "";

    /**
     * METHOD
     */
    HttpMethod[] method() default {HttpMethod.GET};
}
