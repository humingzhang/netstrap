package io.netstrap.core.server.mvc.stereotype.parameter;

import io.netstrap.core.server.http.ParamType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 请求头参数
 * @author minghu.zhang
 */
@Target(value={ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@NameAlias(type = ParamType.REQUEST_HEADER)
public @interface HeaderValue {

    /**
     * 参数名
     */
    @AliasFor(annotation = NameAlias.class)
    String value() default "";
}
