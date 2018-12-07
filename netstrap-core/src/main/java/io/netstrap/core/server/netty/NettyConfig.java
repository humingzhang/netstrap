package io.netstrap.core.server.netty;

import io.netstrap.config.stereotype.Configurable;
import io.netstrap.config.stereotype.Prefix;
import io.netstrap.core.server.config.ServerConfig;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Netty容器配置
 *
 * @author minghu.zhang
 * @date 2018/11/07
 */

@Configurable
@Data
@Prefix("netty")
@EqualsAndHashCode(callSuper = false)
public class NettyConfig extends ServerConfig {

    /**
     * boss线程数
     */
    private int boss = Runtime.getRuntime().availableProcessors();
    /**
     * work线程数
     */
    private int work = Runtime.getRuntime().availableProcessors() * 2;

}



