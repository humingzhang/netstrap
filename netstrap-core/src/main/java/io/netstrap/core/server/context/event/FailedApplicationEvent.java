package io.netstrap.core.server.context.event;

import io.netstrap.core.server.context.NetstrapApplicationEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 启动失败事件
 *
 * @author minghu.zhang
 * @date 2018/11/03
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class FailedApplicationEvent extends NetstrapApplicationEvent {

    private ConfigurableApplicationContext context;
    /**
     * 异常信息
     */
    private Throwable exception;

    /**
     * 创建事件对象
     */
    public FailedApplicationEvent(Object source, ConfigurableApplicationContext context, Throwable exception) {
        super(source);
        this.context = context;
        this.exception = exception;
    }
}
