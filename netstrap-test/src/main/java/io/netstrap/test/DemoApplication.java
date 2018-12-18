package io.netstrap.test;

import io.netstrap.core.server.NetstrapBootApplication;
import io.netstrap.core.server.context.stereotype.EnableNetstrapServer;
import io.netstrap.core.server.context.stereotype.NetstrapApplication;
import io.netstrap.core.server.enums.ProtocolType;
import io.netstrap.core.server.stereotype.NetstrapServer;
import lombok.extern.log4j.Log4j2;

/**
 * 启动宠物程序
 *
 * @author minghu.zhang
 * @date 2018/11/03
 */
@NetstrapApplication
@Log4j2
@EnableNetstrapServer(protocol = ProtocolType.WEB_SOCKET)
public class DemoApplication {

    public static void main(String[] args) {
        NetstrapBootApplication.run(DemoApplication.class, args);
    }

}
