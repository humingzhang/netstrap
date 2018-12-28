package io.netstrap.core.server.mina;

import io.netstrap.core.server.enums.ProtocolType;
import io.netstrap.core.server.server.Server;
import io.netstrap.core.server.stats.Stats;
import io.netstrap.core.server.context.stereotype.NetstrapServer;
import lombok.extern.log4j.Log4j2;

/**
 * Mina服务接口
 *
 * @author minghu.zhang
 * @date 2018/11/05
 */
@Log4j2
@NetstrapServer
public class MinaServer implements Server {

    @Override
    public void start(ProtocolType protocol) {
        log.debug("The server is starting. ");
    }

    @Override
    public void stop() {
        log.debug("the server has stopped.");
    }

    @Override
    public void join() {
        log.debug("The server is monitoring the network event. ");
    }

    @Override
    public Stats.Code stats() {
        return null;
    }

    @Override
    public boolean isStarted() {
        return false;
    }

    @Override
    public boolean isStopped() {
        return false;
    }
}
