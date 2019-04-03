package io.netstrap.core.websocket.stereotype.mapping;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 标识为WebSocket控制器
 *
 * @author minghu.zhang
 */
@Target(value = ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
@Documented
public @interface WebSocketController {
}
