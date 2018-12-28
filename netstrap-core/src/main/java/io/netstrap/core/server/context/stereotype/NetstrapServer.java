package io.netstrap.core.server.context.stereotype;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 标识为网络服务
 *
 * @author minghu.zhang
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface NetstrapServer {

}
