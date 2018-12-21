package io.netstrap.core.server.websocket;

import io.netty.channel.group.ChannelGroup;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 命名线程组
 *
 * @author minghu.zhang
 * @date 2018/12/21 16:05
 */
public class NamedGroup {

    /**
     * Group-ChannelGroupMap
     */
    private static final Map<String, ChannelGroup> GROUP_MAP =
            new ConcurrentHashMap<>(16);

    /**
     * 是否包含分组
     */
    public static boolean containsKey(String key) {
        return GROUP_MAP.containsKey(key);
    }

    /**
     * 获取ChannelGroup
     */
    public static ChannelGroup get(String key) {
        return GROUP_MAP.get(key);
    }

    /**
     * 保存ChannelGroup
     */
    public static void put(String key, ChannelGroup group) {
        GROUP_MAP.put(key, group);
    }
}
