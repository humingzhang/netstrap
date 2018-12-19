package io.netstrap.core.server.mvc.stereotype.parameter;

import io.netstrap.core.server.http.ContextType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 请求URI参数
 *
 * @author minghu.zhang
 */
@Target(value = {ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestValue(type = ContextType.REQUEST_PARAM)
public @interface RequestParam {

    /**
     * 参数名
     */
    @AliasFor(annotation = RequestValue.class)
    String value() default "";
}
