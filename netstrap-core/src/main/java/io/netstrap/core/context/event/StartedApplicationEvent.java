package io.netstrap.core.context.event;

import io.netstrap.core.context.NetstrapApplicationEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * 启动完成事件
 *
 * @author minghu.zhang
 * @date 2018/11/03
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class StartedApplicationEvent extends NetstrapApplicationEvent {

    private ConfigurableApplicationContext context;

    /**
     * 创建事件对象
     */
    public StartedApplicationEvent(Object source, ConfigurableApplicationContext context) {
        super(source);
        this.context = context;
    }
}
