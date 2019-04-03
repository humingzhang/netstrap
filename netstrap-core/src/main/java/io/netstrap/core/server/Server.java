package io.netstrap.core.server;

import io.netstrap.core.enums.ProtocolType;
import io.netstrap.core.stats.Stats;

/**
 * 网络服务容器
 *
 * @author minghu.zhang
 * @date 2018/11/05
 */
public interface Server {

    /**
     * 启动网络服务
     * @param protocol   协议类型
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied,
     *  and the thread is interrupted, either before or during the activity.
     */
    void start(ProtocolType protocol) throws InterruptedException;

    /**
     * 停止服务
     */
    void stop();

    /**
     * 同步监听
     * @throws InterruptedException Thrown when a thread is waiting, sleeping, or otherwise occupied,
     *  and the thread is interrupted, either before or during the activity.
     */
    void join() throws InterruptedException;

    /**
     * 获取状态
     * @return 服务状态
     */
    Stats.Code stats();

    /**
     * 是否已启动
     * @return boolean
     */
    boolean isStarted();

    /**
     * 是否已停止
     * @return boolean
     */
    boolean isStopped();
}
