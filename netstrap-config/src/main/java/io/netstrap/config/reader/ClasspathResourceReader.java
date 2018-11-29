package io.netstrap.config.reader;

import io.netstrap.config.ConfigHandler;
import io.netstrap.config.policy.ProtocolPolicy;
import io.netstrap.config.stereotype.ProtocolSupport;

import java.io.InputStream;

/**
 * 类路径资源读取
 * @author minghu.zhang
 * @date 2018/11/02
 */
@ProtocolSupport(protocol = ProtocolPolicy.CLASSPATH)
public class ClasspathResourceReader implements ConfigHandler.ResourceReader {

    @Override
    public InputStream load(String path) {
        return this.getClass().getResourceAsStream(path);
    }
}
