package io.netstrap.core.context.stereotype;

import java.lang.annotation.*;

/**
 * 启动主类
 *
 * @author minghu.zhang
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface NetstrapApplication {

    /**
     * 待扫描的包名列表
     */
    String[] packages() default {};

    /**
     * 默认spring配置文件
     */
    String[] configLocations() default {"classpath*:config.xml"};
}
