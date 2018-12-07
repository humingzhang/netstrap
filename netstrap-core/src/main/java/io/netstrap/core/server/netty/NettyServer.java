package io.netstrap.core.server.netty;

import io.netstrap.common.NetstrapConstant;
import io.netstrap.core.server.enums.ProtocolType;
import io.netstrap.core.server.server.Server;
import io.netstrap.core.server.netty.initializer.HttpChannelInitializer;
import io.netstrap.core.server.stats.Stats;
import io.netstrap.core.server.stereotype.NetstrapServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.util.ResourceLeakDetector;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Netty服务接口
 *
 * @author minghu.zhang
 * @date 2018/11/05
 */
@Log4j2
@NetstrapServer
public class NettyServer implements Server {

    /**
     * bootstrap工具类
     */
    private BootKit bootKit = BootKit.of();
    /**
     * The result of an asynchronous {@link Channel} I/O operation.
     */
    private ChannelFuture sync;
    /**
     * netty配置
     */
    private final NettyConfig nettyServerConfig;
    /**
     * 服务状态
     */
    private Stats status = new Stats();
    /**
     * HTTP报文加工管道
     */
    private final HttpChannelInitializer httpChannelInitializer;

    @Autowired
    public NettyServer(NettyConfig nettyServerConfig, HttpChannelInitializer httpChannelInitializer) {
        this.nettyServerConfig = nettyServerConfig;
        this.httpChannelInitializer = httpChannelInitializer;
    }

    @Override
    public void start(ProtocolType protocol) throws InterruptedException {
        ResourceLeakDetector.setLevel(ResourceLeakDetector.Level.DISABLED);

        int boss = nettyServerConfig.getBoss();
        int work = nettyServerConfig.getWork();

        //创建线程组
        bootKit.createServerBootstrap(boss, work);
        ServerBootstrap bootstrap = bootKit.getBootstrap();

        // 绑定处理器
        applyChildHandler(bootstrap, protocol);

        // 绑定端口
        int port = nettyServerConfig.getPort();
        String ip = nettyServerConfig.getIp();

        if (StringUtils.isEmpty(ip) || NetstrapConstant.ANY_ADDRESS.equals(ip)) {
            sync = bootstrap.bind(port).sync();
        } else {
            sync = bootstrap.bind(ip, port).sync();
        }

        status.setCode(Stats.Code.START);
        log.info("The server bind IP:" + ip + " , PORT:" + port);
    }

    /**
     * 设置网络IO处理，默认实现HTTP
     */
    private void applyChildHandler(ServerBootstrap bootstrap, ProtocolType protocol) {
        if (protocol.equals(ProtocolType.HTTP)) {
            bootstrap.childHandler(httpChannelInitializer);
        } else if (protocol.equals(ProtocolType.SOCKET)) {
            //TODO
        } else if (protocol.equals(ProtocolType.WEB_SOCKET)) {
            //TODO
        }
    }

    @Override
    public void stop() {
        log.info("The server has stopped.");
        status.setCode(Stats.Code.STOP);
        bootKit.getBossGroup().shutdownGracefully();
        bootKit.getWorkGroup().shutdownGracefully();
    }

    @Override
    public void join() throws InterruptedException {
        status.setCode(Stats.Code.SYNC);
        sync.channel().closeFuture().sync();
    }

    @Override
    public Stats.Code stats() {
        return status.getCode();
    }

    @Override
    public boolean isStarted() {
        return status.getCode().equals(Stats.Code.SYNC);
    }

    @Override
    public boolean isStopped() {
        return status.getCode().equals(Stats.Code.STOP);
    }

}
