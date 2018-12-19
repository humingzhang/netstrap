package io.netstrap.core.server.http.mvc.stereotype.mapping;

import io.netstrap.core.server.http.HttpMethod;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 标识DELETE请求
 *
 * @author minghu.zhang
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = HttpMethod.DELETE)
public @interface DeleteMapping {
    /**
     * uri
     */
    @AliasFor(annotation = RequestMapping.class)
    String value();
}
