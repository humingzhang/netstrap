package io.netstrap.core.server.websocket.filter;

import io.netstrap.common.factory.ClassFactory;
import io.netstrap.core.server.stereotype.Filterable;
import io.netstrap.core.server.websocket.WebSocketContext;
import io.netstrap.core.server.websocket.WebSocketFilter;
import io.netstrap.core.server.websocket.router.WebSocketAction;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * 过滤器执行器
 *
 * @author minghu.zhang
 * @date 2018/11/08
 */
@Component
public class DefaultWebSocketFilter {

    /**
     * 过滤组
     */
    private List<WebSocketFilter> filters = new ArrayList<>();

    /**
     * 类工程
     */
    private ClassFactory factory;

    /**
     * 对象工厂
     */
    private final ApplicationContext context;

    @Autowired
    public DefaultWebSocketFilter(ConfigurableApplicationContext context) {
        this.context = context;
    }

    /**
     * 初始化过滤器
     */
    @PostConstruct
    public void init() {
        factory = ClassFactory.getInstance();
        initFilterChain();
    }

    /**
     * 初始化过滤器链
     */
    private void initFilterChain() {
        List<Pair<Integer, Class<?>>> ordered = getOrderedList();

        for (Pair<Integer, Class<?>> pair : ordered) {
            WebSocketFilter filter = (WebSocketFilter) context.getBean(pair.getValue1());
            filters.add(filter);
        }
    }

    /**
     * 获取排序列表
     */
    private List<Pair<Integer, Class<?>>> getOrderedList() {
        List<Pair<Integer, Class<?>>> filters = new ArrayList<>();

        //获取待排序列表
        List<Class<?>> classes = factory.getClassByInterface(WebSocketFilter.class);
        for (Class<?> clz : classes) {
            Filterable filtered = clz.getAnnotation(Filterable.class);
            int order = filtered.order();
            filters.add(Pair.with(order, clz));
        }

        //过滤器排序
        filters.sort(new Comparator<Pair<Integer, Class<?>>>() {
            @Override
            public int compare(Pair<Integer, Class<?>> a, Pair<Integer, Class<?>> b) {
                if (a.getValue0() > b.getValue0()) {
                    return -1;
                } else if (a.getValue0() < b.getValue0()) {
                    return 1;
                }
                return 0;
            }
        });

        return filters;
    }

    /**
     * 过滤器
     *
     * @param channel 链接通道
     * @param context 上下文
     * @param frame   请求报文
     * @return 执行结果，是否需要继续执行
     * @throws Exception 解析异常
     */
    public boolean filter(Channel channel, WebSocketContext context, WebSocketFrame frame) throws Exception {
        for (WebSocketFilter filter : filters) {
            if (!filter.filter(channel, context, frame)) {
                return false;
            }
        }
        return true;
    }
}
