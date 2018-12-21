package io.netstrap.core.server.context.stereotype;

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
     * 默认spring配置文件
     */
    String[] configLocations() default {"classpath*:application.xml"};
}
