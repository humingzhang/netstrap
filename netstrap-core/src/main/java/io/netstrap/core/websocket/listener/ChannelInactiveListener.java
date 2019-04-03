package io.netstrap.core.websocket.listener;

import io.netty.channel.Channel;

/**
 * 链接不可用监听
 *
 * @author minghu.zhang
 * @date 2018/12/21 16:24
 */
public interface ChannelInactiveListener {


    /**
     * 当通道处于活动状态时，通知监听者
     *
     * @param channel 通道对象
     */
    void channelActive(Channel channel);

    /**
     * 当通道不可用时，通知监听者
     *
     * @param channel 通道对象
     */
    void channelInactive(Channel channel);

}
