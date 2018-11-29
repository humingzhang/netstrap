package io.netstrap.core.context.stereotype;

import io.netstrap.config.policy.ProtocolPolicy;

import java.lang.annotation.*;

/**
 * 启动主类
 * @author minghu.zhang
 */
@Target(value=ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NetstrapApplication {

    /**
     * 待扫描的包名列表
     * @return
     */
    String[] packages() default {};
}
