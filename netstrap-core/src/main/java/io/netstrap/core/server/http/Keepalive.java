package io.netstrap.core.server.http;

/**
 * keep_alive参数
 *
 * @author minghu.zhang
 * @date 2018/12/1 12:46
 */
public interface Keepalive {
    /**
     * HTTP1.1 关闭keep-alive
     */
    String CLOSE_ALIVE = "close";
    /**
     * HTTP1.0 打开keep-alive
     */
    String KEEP_ALIVE = "keep-alive";
}
