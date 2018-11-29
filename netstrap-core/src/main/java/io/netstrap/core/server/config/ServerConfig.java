package io.netstrap.core.server.config;

import io.netstrap.config.stereotype.Prefix;
import lombok.Data;

/**
 * 服务基本配置
 * @author minghu.zhang
 * @date 2018/11/07
 */
@Data
@Prefix("server")
public class ServerConfig {
    /**
     * 启动端口
     */
    private int    port = 8080;
    /**
     * 服务名称
     */
    private String name = "application";
    /**
     * 绑定的地址
     */
    private String ip   = "127.0.0.1";
    /**
     * SSL配置
     */
    private SSLConfig ssl;
}
