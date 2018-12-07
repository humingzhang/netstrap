package io.netstrap.test;

import io.netstrap.core.NetstrapBootApplication;
import io.netstrap.core.context.stereotype.NetstrapApplication;
import lombok.extern.log4j.Log4j2;

/**
 * 启动宠物程序
 *
 * @author minghu.zhang
 * @date 2018/11/03
 */
@NetstrapApplication
@Log4j2
public class DemoApplication {

    public static void main(String[] args) {
        NetstrapBootApplication.run(DemoApplication.class, args);
    }

}
