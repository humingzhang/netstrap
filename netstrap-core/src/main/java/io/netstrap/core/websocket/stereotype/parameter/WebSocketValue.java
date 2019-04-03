package io.netstrap.core.websocket.stereotype.parameter;

import io.netstrap.core.websocket.router.WebSocketContextType;

import java.lang.annotation.*;

/**
 * WebSocket参数值获取
 *
 * @author minghu.zhang
 */
@Target(value = {ElementType.ANNOTATION_TYPE, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebSocketValue {

    /**
     * 参数名
     */
    String value() default "";

    /**
     * 参数值域
     */
    WebSocketContextType type();
}
