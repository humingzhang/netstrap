package io.netstrap.config.stereotype;

import io.netstrap.config.policy.ProtocolPolicy;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 指定自动配置类
 * @author minghu.zhang
 */
@Target(value=ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface Configurable {
    /**
     * classpath： 表示读取类路径（默认）
     * local:     表示读取文件路径
     * http：     表示读取网络地址
     * @return ProtocolPolicy
     */
    ProtocolPolicy protocol() default ProtocolPolicy.CLASSPATH;

    /**
     * 配置文件名
     * @return String
     */
    String   path()    default "/application.yml";
}
