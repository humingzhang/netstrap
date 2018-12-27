package io.netstrap.core.server.http.stereotype;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * Filter类注解，提供过滤器调用顺序
 */
@Target(value = {ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface Filterable {
    int order() default 0;
}
