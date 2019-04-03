package io.netstrap.config.stereotype;

import java.lang.annotation.*;

/**
 * 定义获取参数值的表达式
 *
 * @author minghu.zhang
 */
@Target(value = ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {

    /**
     * 指定获取属性名为x.y.z的值，若不指定表达式将默认使用 "前缀.属性名"
     * ${x.y.z}
     */
    String value();

}
