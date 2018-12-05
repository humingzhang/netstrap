package io.netstrap.core.server.mvc.stereotype;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 标识为控制器
 * @author minghu.zhang
 */
@Target(value=ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface RestController {
}
