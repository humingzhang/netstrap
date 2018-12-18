package io.netstrap.core.server.server;

import io.netstrap.core.server.enums.ProtocolType;
import io.netstrap.core.server.stats.Stats;

/**
 * 网络服务容器
 *
 * @author minghu.zhang
 * @date 2018/11/05
 */
public interface Server {

    /**
     * 启动服务
     */
    void start(ProtocolType protocol) throws InterruptedException;

    /**
     * 停止服务
     */
    void stop();

    /**
     * 同步监听
     */
    void join() throws InterruptedException;

    /**
     * 获取状态
     */
    Stats.Code stats();

    /**
     * 是否已启动
     */
    boolean isStarted();

    /**
     * 是否已停止
     */
    boolean isStopped();
}
