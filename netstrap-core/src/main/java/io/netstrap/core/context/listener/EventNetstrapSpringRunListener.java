package io.netstrap.core.context.listener;

import io.netstrap.core.NetstrapBootApplication;
import io.netstrap.core.context.NetstrapSpringRunListener;
import io.netstrap.core.context.event.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.SimpleApplicationEventMulticaster;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.util.ErrorHandler;

/**
 * 默认Spring容器运行监听器
 *
 * @author minghu.zhang
 * @date 2018/11/03
 */
@Log4j2
public class EventNetstrapSpringRunListener implements NetstrapSpringRunListener, Ordered {

    private final NetstrapBootApplication application;

    /**
     * Spring事件通知
     */
    private final SimpleApplicationEventMulticaster initialMulticaster;

    /**
     * Netstrap事件监听器
     */
    public EventNetstrapSpringRunListener(NetstrapBootApplication application) {
        this.application = application;
        this.initialMulticaster = new SimpleApplicationEventMulticaster();
        for (ApplicationListener<?> listener : application.getListeners()) {
            this.initialMulticaster.addApplicationListener(listener);
        }
    }

    @Override
    public int getOrder() {
        return 0;
    }

    @Override
    public void starting() {
        this.initialMulticaster.multicastEvent(
                new StartingApplicationEvent(this.application));
    }

    @Override
    public void contextPrepare(ConfigurableApplicationContext context) {
        context.publishEvent(
                new ContextPrepareEvent(this.application, context));
    }

    @Override
    public void started(ConfigurableApplicationContext context) {
        context.publishEvent(
                new StartedApplicationEvent(this.application, context));
    }

    @Override
    public void failed(ConfigurableApplicationContext context, Throwable exception) {
        FailedApplicationEvent event = new FailedApplicationEvent(this.application, context, exception);
        if (context != null && context.isActive()) {
            context.publishEvent(event);
        } else {
            if (context instanceof AbstractApplicationContext) {
                for (ApplicationListener<?> listener : ((AbstractApplicationContext) context)
                        .getApplicationListeners()) {
                    this.initialMulticaster.addApplicationListener(listener);
                }
            }

            this.initialMulticaster.setErrorHandler(new LoggingErrorHandler());
            this.initialMulticaster.multicastEvent(event);
        }
    }

    private static class LoggingErrorHandler implements ErrorHandler {

        @Override
        public void handleError(Throwable throwable) {
            log.warn("Error calling ApplicationEventListener", throwable);
        }

    }
}
