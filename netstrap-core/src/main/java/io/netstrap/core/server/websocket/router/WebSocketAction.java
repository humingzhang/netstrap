package io.netstrap.core.server.websocket.router;


import lombok.Data;

import java.lang.reflect.Method;

/**
 * WebSocket调用模型
 *
 * @author minghu.zhang
 * @date 2018/12/05
 */
@Data
public class WebSocketAction {
    /**
     * 参数映射
     */
    private WebSocketParamMapping[] mappings;
    /**
     * 当前映射的URI
     */
    private String uri;
    /**
     * 调用对象
     */
    private Object invoker;
    /**
     * 调用方法
     */
    private Method action;
}
