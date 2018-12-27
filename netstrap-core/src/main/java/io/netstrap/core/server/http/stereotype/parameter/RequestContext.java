package io.netstrap.core.server.http.stereotype.parameter;

import io.netstrap.core.server.http.router.HttpContextType;
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
@RequestValue(type = HttpContextType.REQUEST_CONTEXT)
public @interface RequestContext {

    /**
     * 参数名
     */
    @AliasFor(annotation = RequestValue.class)
    String value() default "";
}
