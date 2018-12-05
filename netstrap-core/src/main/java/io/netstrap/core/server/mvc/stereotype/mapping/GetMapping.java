package io.netstrap.core.server.mvc.stereotype.mapping;

import io.netstrap.core.server.http.HttpMethod;
import org.springframework.core.annotation.AliasFor;

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
    /**
     * uri
     */
    @AliasFor(annotation = RequestMapping.class)
    String value();
}
