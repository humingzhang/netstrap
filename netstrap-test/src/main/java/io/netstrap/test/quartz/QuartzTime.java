package io.netstrap.test.quartz;

import io.netstrap.core.server.websocket.DefaultGroup;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时器测试
 *
 * @author minghu.zhang
 */
@Component
@Log4j2
public class QuartzTime {

    /**
     * 执行刷新间隔
     */
    @Scheduled(cron = "0/1 * * * * ?")
    public void loopSayHello() {
        String message = "{\"timestamp\":"+System.currentTimeMillis()+",\"linking\":"+ DefaultGroup.count()+"}";
        System.out.println(message);
        DefaultGroup.push(message);
    }
}
