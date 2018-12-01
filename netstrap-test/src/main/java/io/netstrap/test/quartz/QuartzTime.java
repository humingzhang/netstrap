package io.netstrap.test.quartz;

import lombok.extern.log4j.Log4j2;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时器测试
 * @author minghu.zhang
 */
@Component
@Log4j2
public class QuartzTime {
	
	/**
	 * 执行刷新间隔
	 */
	@Scheduled(cron="0/3 * * * * ?")
	public void loopSayHello() {
		System.out.println("hello ... world");
	}
}
