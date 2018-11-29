package io.netstrap.config.stereotype;

import java.lang.annotation.*;

/**
 * 定义默认获取参数的前缀
 * @author minghu.zhang
 */
@Target(value=ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Prefix {
    /**
     * 指定获取属性前缀，若不指定将默认使用类名小写，若有驼峰标识，将替换为点(.)
     * 例如 @Prefix("server") 将获取server下的数据，UserProfile 将获取user.profile下的数据
     */
    String value();
}
