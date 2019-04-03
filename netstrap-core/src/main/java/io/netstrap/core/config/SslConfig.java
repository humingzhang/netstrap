package io.netstrap.core.config;

import io.netstrap.config.stereotype.Prefix;
import lombok.Data;

/**
 * SSL配置
 *
 * @author minghu.zhang
 * @date 2018/11/07
 */
@Data
@Prefix("server.ssl")
public class SslConfig {
    /**
     * 是否开启SSL
     */
    private boolean enable = false;
    /**
     * jks密码
     */
    private String jksPwd;
    /**
     * jks密钥路径
     */
    private String jksPath;
}
