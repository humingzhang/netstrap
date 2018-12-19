package io.netstrap.test.quartz;

import io.netstrap.core.server.websocket.WebSocketGroup;
import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.channels.Channel;

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
        String message = "{\"timestamp\":"+System.currentTimeMillis()+",\"linking\":"+WebSocketGroup.count()+"}";
        WebSocketGroup.push(message);
    }
}
