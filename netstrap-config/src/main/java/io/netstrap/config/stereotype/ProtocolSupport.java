package io.netstrap.config.stereotype;

import io.netstrap.config.policy.ProtocolPolicy;

import java.lang.annotation.*;

/**
 * 标识资源加载器所支持的协议类型
 * @author minghu.zhang
 */
@Target(value=ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ProtocolSupport {

    /**
     * 标识该类所支持的类型
     */
    ProtocolPolicy protocol();
}
