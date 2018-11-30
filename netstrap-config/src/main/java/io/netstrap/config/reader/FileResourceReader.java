package io.netstrap.config.reader;

import io.netstrap.config.ConfigHandler;
import io.netstrap.config.policy.ProtocolPolicy;
import io.netstrap.config.stereotype.ProtocolSupport;

import java.io.FileInputStream;
import java.io.InputStream;

/**
 * 本地磁盘文件加载
 * @author minghu.zhang
 * @date 2018/11/02
 */
@ProtocolSupport(protocol = ProtocolPolicy.LOCAL)
public class FileResourceReader implements ConfigHandler.ResourceReader {

    @Override
    public InputStream load(String path) {
        try {
            return new FileInputStream(path);
        } catch (Exception e) {
            throw new RuntimeException("the file \""+path+"\" isn't exits.");
        }
    }

}
