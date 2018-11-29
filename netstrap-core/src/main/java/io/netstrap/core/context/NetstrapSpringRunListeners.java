package io.netstrap.core.context;

import lombok.extern.log4j.Log4j2;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.util.ReflectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 执行一组Spring运行监听器
 * @author minghu.zhang
 * @date 2018/11/03
 */
@Log4j2
public class NetstrapSpringRunListeners {

    private List<NetstrapSpringRunListener> listeners;

    public NetstrapSpringRunListeners(List<NetstrapSpringRunListener> listeners) {
        this.listeners = listeners;
    }

    public NetstrapSpringRunListeners() {
        this.listeners = new ArrayList<>();
    }

    public void starting() {
        for (NetstrapSpringRunListener listener:listeners) {
            listener.starting();
        }
    }

    public void contextPrepare(ConfigurableApplicationContext context) {
        for (NetstrapSpringRunListener listener:listeners) {
            listener.contextPrepare(context);
        }
    }

    public void started(ConfigurableApplicationContext context) {
        for (NetstrapSpringRunListener listener:listeners) {
            listener.started(context);
        }
    }

    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        for (NetstrapSpringRunListener listener:listeners) {
            callFailedListener(listener, context, exception);
        }
    }

    private void callFailedListener(NetstrapSpringRunListener listener,
                                    ConfigurableApplicationContext context, Throwable exception) {
        try {
            listener.failed(context, exception);
        }
        catch (Throwable ex) {
            if (exception == null) {
                ReflectionUtils.rethrowRuntimeException(ex);
            }
            if (log.isDebugEnabled()) {
                log.error("Error handling failed", ex);
            }
            else {
                String message = ex.getMessage();
                message = (message != null) ? message : "no error message";
                log.warn("Error handling failed (" + message + ")");
            }
        }
    }

}
