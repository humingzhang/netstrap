package io.netstrap.core.websocket.stereotype.mapping;

import java.lang.annotation.*;

/**
 * 标识WebSocket请求URI
 *
 * @author minghu.zhang
 */
@Target(value = {ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebSocketMapping {
    /**
     * URI
     */
    String value() default "";
}
