package io.netstrap.core.server.websocket;

import io.netstrap.common.factory.ClassFactory;
import io.netstrap.core.server.websocket.listener.ChannelInactiveListener;
import io.netty.channel.Channel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 监听启动器
 *
 * @author minghu.zhang
 * @date 2018/12/21 16:32
 */
@Component
public class ChannelInactiveRunListener {

    /**
     * 程序上下文
     */
    private final ApplicationContext context;
    /**
     * 监听组
     */
    private List<ChannelInactiveListener> listeners = new ArrayList<>();

    @Autowired
    public ChannelInactiveRunListener(ApplicationContext context) {
        this.context = context;
    }

    @PostConstruct
    private void initListener() {
        ClassFactory factory = ClassFactory.getInstance();
        factory.getClassByInterface(ChannelInactiveListener.class)
                .forEach(clz -> listeners.add((ChannelInactiveListener) context.getBean(clz)));
    }

    /**
     * inactive 通知
     *
     * @param channel 连接通道
     */
    public void channelInactive(Channel channel) {
        listeners.forEach(listener -> listener.channelInactive(channel));
    }

    /**
     * active 通知
     *
     * @param channel 连接通道
     */
    public void channelActive(Channel channel) {
        listeners.forEach(listener -> listener.channelActive(channel));
    }
}
