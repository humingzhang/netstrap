package io.netstrap.core.server.context.event;

import io.netstrap.core.server.context.NetstrapApplicationEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Spring容器启动完毕事件
 *
 * @author minghu.zhang
 * @date 2018/11/05
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class ContextPrepareEvent extends NetstrapApplicationEvent {

    private ConfigurableApplicationContext context;

    /**
     * 创建事件对象
     */
    public ContextPrepareEvent(Object source, ConfigurableApplicationContext context) {
        super(source);
        this.context = context;
    }
}
