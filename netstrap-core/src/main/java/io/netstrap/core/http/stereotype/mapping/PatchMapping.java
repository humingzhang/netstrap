package io.netstrap.core.http.stereotype.mapping;

import io.netstrap.core.http.router.HttpMethod;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 标识PATCH请求
 *
 * @author minghu.zhang
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestMapping(method = HttpMethod.PATCH)
public @interface PatchMapping {
    /**
     * uri
     */
    @AliasFor(annotation = RequestMapping.class)
    String value();
}
