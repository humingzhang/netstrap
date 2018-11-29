package io.netstrap.test.config;

import io.netstrap.config.stereotype.Configurable;
import io.netstrap.config.stereotype.Prefix;
import lombok.Data;

/**
 * @Description 微信配置
 * @author minghu.zhang
 * @date 2018/11/29 14:07
 */
@Configurable
@Prefix("wechat")
@Data
public class WechatConfig {

    private String accessKey;
    private String accessValue;
    private int    indexes;
    private String requestUri;

}
