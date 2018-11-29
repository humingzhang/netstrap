package io.netstrap.core.context;

import io.netstrap.core.NetstrapBootApplication;
import org.springframework.context.ApplicationEvent;

/**
 * 通用事件模型
 * @author minghu.zhang
 * @date 2018/11/03
 */
public class NetstrapApplicationEvent extends ApplicationEvent {

    /**
     * 创建事件对象
     */
    public NetstrapApplicationEvent(Object source) {
        super(source);
    }

    /**
     * 获取当前启动应用环境
     */
    public NetstrapBootApplication getApplication() {
        return (NetstrapBootApplication)source;
    }
}
