package io.netstrap.core.server.netty;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollEventLoopGroup;
import io.netty.channel.epoll.EpollServerSocketChannel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import lombok.Data;

/**
 * NettyStrap工具
 * @author minghu.zhang
 * @date 2018/12/3 11:24
 */
@Data
class BootKit {

    private ServerBootstrap bootstrap;
    private EventLoopGroup  bossGroup;
    private EventLoopGroup  workGroup;

    /**
     * private constructor
     */
    private BootKit(){}

    /**
     * 获取单例对象
     */
    static BootKit of() {
        return new BootKit();
    }

    /**
     * 创建bootstrap
     * @param boss 连接线程数
     * @param work 工作线程数
     */
    public BootKit createServerBootstrap(int boss, int work) {
        bootstrap = new ServerBootstrap();
        if(epollIsAvailable()) {
            createEpollGroup(boss,work);
        } else {
            createNioGroup(boss,work);
        }

        bootstrap.option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
                .childOption(ChannelOption.SO_KEEPALIVE, true);

        return this;
    }

    /**
     * 创建epoll线程组
     */
    private void createEpollGroup(int boss,int work) {
        bossGroup   = new EpollEventLoopGroup(boss, new DefaultThreadFactory("epoll-boss@"));
        workGroup = new EpollEventLoopGroup(work, new DefaultThreadFactory("epoll-worker@"));
        bootstrap.group(bossGroup,workGroup)
                .channel(EpollServerSocketChannel.class);
    }

    /**
     * 创建NIO线程组
     */
    private void createNioGroup(int boss,int work) {
        bossGroup = new NioEventLoopGroup(boss, new DefaultThreadFactory("nio-boss@"));
        workGroup = new NioEventLoopGroup(work, new DefaultThreadFactory("nio-work@"));
        bootstrap.group(bossGroup,workGroup)
                .channel(NioServerSocketChannel.class);
    }

    /**
     * 是否支持epoll
     */
    private boolean epollIsAvailable() {
        try {
            Object obj = Class.forName("io.netty.channel.epoll.Epoll").getMethod("isAvailable").invoke(null);
            return null != obj && Boolean.valueOf(obj.toString()) && System.getProperty("os.name").toLowerCase().contains("linux");
        } catch (Exception e) {
            return false;
        }
    }

}
