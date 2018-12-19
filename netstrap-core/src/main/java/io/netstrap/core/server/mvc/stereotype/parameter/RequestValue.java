package io.netstrap.core.server.mvc.stereotype.parameter;

import io.netstrap.core.server.http.ContextType;

import java.lang.annotation.*;

/**
 * 参数值获取
 *
 * @author minghu.zhang
 */
@Target(value = {ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestValue {

    /**
     * 参数名
     */
    String value() default "";

    /**
     * METHOD
     */
    ContextType type();
}
