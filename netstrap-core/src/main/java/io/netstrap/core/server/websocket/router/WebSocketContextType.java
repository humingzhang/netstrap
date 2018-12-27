package io.netstrap.core.server.websocket.router;


/**
 * WebSocket值域类型
 *
 * @author minghu.zhang
 * @date 2018/11/07
 */
public enum WebSocketContextType {

    /**
     * URI参数
     */
    REQUEST_PARAM,
    /**
     * 请求体
     */
    REQUEST_BODY,
    /**
     * 请求属性
     */
    REQUEST_ATTRIBUTE
}
