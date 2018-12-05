package io.netstrap.core.server.mvc.stereotype.parameter;

import io.netstrap.core.server.http.ParamType;

import java.lang.annotation.*;

/**
 * 参数别名
 * @author minghu.zhang
 */
@Target(value={ElementType.ANNOTATION_TYPE,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NameAlias {

    /**
     * 参数名
     */
    String value() default "";

    /**
     * METHOD
     */
    ParamType type();
}
