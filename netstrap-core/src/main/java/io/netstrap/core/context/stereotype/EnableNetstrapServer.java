package io.netstrap.core.context.stereotype;

import io.netstrap.core.context.enums.ProtocolType;
import io.netstrap.core.context.enums.ServerType;

import java.lang.annotation.*;

/**
 * 标注网络服务
 * @author minghu.zhang
 */
@Target(value=ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface EnableNetstrapServer {

    /**
     * 待扫描的包名列表
     * @return
     */
    ServerType    serverType() default ServerType.Netty;

    /**
     * 协议类型
     * @return
     */
    ProtocolType  protocol() default ProtocolType.HTTP;
}
