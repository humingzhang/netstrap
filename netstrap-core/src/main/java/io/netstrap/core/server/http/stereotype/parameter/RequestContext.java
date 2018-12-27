package io.netstrap.core.server.http.stereotype.parameter;

import io.netstrap.core.server.http.ContextType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 请求上下文参数
 *
 * @author minghu.zhang
 */
@Target(value = {ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@RequestValue(type = ContextType.REQUEST_CONTEXT)
public @interface RequestContext {

    /**
     * 参数名
     */
    @AliasFor(annotation = RequestValue.class)
    String value() default "";
}
