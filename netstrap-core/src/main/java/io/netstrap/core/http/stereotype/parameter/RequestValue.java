package io.netstrap.core.http.stereotype.parameter;

import io.netstrap.core.http.router.HttpContextType;

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
     * 参数值域
     */
    HttpContextType type();
}
