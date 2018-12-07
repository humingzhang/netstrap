package io.netstrap.config.reader;

import io.netstrap.config.ConfigHandler;
import io.netstrap.config.policy.ProtocolPolicy;
import io.netstrap.config.stereotype.ProtocolSupport;

import java.io.InputStream;

/**
 * 远程资源读取
 *
 * @author minghu.zhang
 * @date 2018/11/02
 */
@ProtocolSupport(protocol = ProtocolPolicy.REMOTE)
public class RemoteResourceReader implements ConfigHandler.ResourceReader {

    @Override
    public InputStream load(String path) {
        //TODO
        return null;
    }

}
