package io.netstrap.core.server.mvc.stereotype.mapping;

import io.netstrap.core.server.http.HttpMethod;
import org.springframework.core.annotation.AliasFor;

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
    /**
     * uri
     */
    @AliasFor(annotation = RequestMapping.class)
    String value();
}
