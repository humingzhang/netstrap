package io.netstrap.core.context;

import io.netstrap.core.context.event.StartedApplicationEvent;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * @Description Logo打印实现
 * @author minghu.zhang
 * @date 2018/11/29 14:37
 */
@Log4j2
public class LogoApplicationListener implements ApplicationListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {

        if(event instanceof StartedApplicationEvent) {

            log.info("");

        }

    }

}
