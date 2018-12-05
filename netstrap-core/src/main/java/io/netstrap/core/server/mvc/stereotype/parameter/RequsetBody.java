package io.netstrap.core.server.mvc.stereotype.parameter;

import io.netstrap.core.server.http.ParamType;
import org.springframework.core.annotation.AliasFor;

import java.lang.annotation.*;

/**
 * 请求体参数
 * @author minghu.zhang
 */
@Target(value={ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@NameAlias(type = ParamType.REQUEST_BODY)
public @interface RequsetBody {

    /**
     * 参数名
     */
    @AliasFor(annotation = NameAlias.class)
    String value() default "";
}
