package io.netstrap.core.server.netty;

import io.netstrap.common.NetstrapConstant;
import io.netstrap.core.server.constants.ProtocolType;
import io.netstrap.core.server.Server;
import io.netstrap.core.server.netty.initializer.HttpChannelInitializer;
import io.netstrap.core.server.stats.Stats;
import io.netstrap.core.server.stereotype.NetstrapServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ResourceLeakDetector;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

/**
 * Netty服务接口
 * @author minghu.zhang
 * @date 2018/11/05
 */
@Log4j2
@NetstrapServer
public class NettyServer implements Server {

    /**
     * 线程组
     */
    private EventLoopGroup    bossGroup;
    private EventLoopGroup    workGroup;
    private ChannelFuture     sync;
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

        status.setCode(Stats.Code.START);
        /**
         * 创建NIO线程组
         */
        bossGroup = new NioEventLoopGroup(nettyServerConfig.getBoss(),new DefaultThreadFactory("boss"));
        workGroup = new NioEventLoopGroup(nettyServerConfig.getWork(),new DefaultThreadFactory("work"));

        ServerBootstrap bootstrap = new ServerBootstrap();
        bootstrap.group(bossGroup, workGroup)
                .channel(NioServerSocketChannel.class)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        // 绑定处理器
        applyChildHandler(bootstrap,protocol);

        // 绑定端口
        int port  = nettyServerConfig.getPort();
        String ip = nettyServerConfig.getIp();

        if(StringUtils.isEmpty(ip) || NetstrapConstant.ANY_ADDRESS.equals(ip)) {
            sync = bootstrap.bind(port).sync();
        } else {
            sync = bootstrap.bind(ip,port).sync();
        }

        log.info("The server bind IP:"+ip+" , PORT:" + port);
    }

    /**
     * 设置网络IO处理，默认实现HTTP
     * @param bootstrap
     * @param protocol
     */
    private void applyChildHandler(ServerBootstrap bootstrap, ProtocolType protocol) {
        if(protocol.equals(ProtocolType.HTTP)) {
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
        bossGroup.shutdownGracefully();
        workGroup.shutdownGracefully();
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
