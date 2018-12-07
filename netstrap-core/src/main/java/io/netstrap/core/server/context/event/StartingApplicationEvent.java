package io.netstrap.core.server.context.event;

import io.netstrap.core.server.context.NetstrapApplicationEvent;
import lombok.EqualsAndHashCode;
import lombok.Value;

/**
 * 启动服务事件（启动开始）
 *
 * @author minghu.zhang
 * @date 2018/11/03
 */
@Value
@EqualsAndHashCode(callSuper = false)
public class StartingApplicationEvent extends NetstrapApplicationEvent {

    /**
     * 创建事件对象
     *
     * @param source 事件携带参数
     */
    public StartingApplicationEvent(Object source) {
        super(source);
    }
}
