package io.netstrap.core.server.mvc.filter;

import io.netstrap.common.factory.ClassFactory;
import io.netstrap.core.server.http.datagram.HttpRequest;
import io.netstrap.core.server.http.datagram.HttpResponse;
import io.netstrap.core.server.mvc.Filterable;
import io.netstrap.core.server.mvc.WebFilter;
import org.javatuples.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * 过滤器执行器
 *
 * @author minghu.zhang
 * @date 2018/11/08
 */
@Component
public class DefaultWebFilter implements WebFilter {

    /**
     * 过滤组
     */
    private List<WebFilter> filters = new ArrayList<>();

    /**
     * 类工程
     */
    private ClassFactory factory;

    /**
     * 对象工厂
     */
    private final ApplicationContext context;

    @Autowired
    public DefaultWebFilter(ConfigurableApplicationContext context) {
        this.context = context;
    }

    /**
     * 初始化过滤器
     */
    @PostConstruct
    public void init() throws IllegalAccessException, InstantiationException {
        factory = ClassFactory.getInstance();
        initFilterChain();
    }

    /**
     * 初始化过滤器链
     */
    private void initFilterChain() throws IllegalAccessException, InstantiationException {
        List<Pair<Integer, Class<?>>> ordered = getOrderedList();

        for (Pair<Integer, Class<?>> pair : ordered) {
            WebFilter filter = (WebFilter) context.getBean(pair.getValue1());
            filters.add(filter);
        }
    }

    /**
     * 获取排序列表
     */
    private List<Pair<Integer, Class<?>>> getOrderedList() {
        List<Pair<Integer, Class<?>>> filters = new ArrayList<>();

        //获取待排序列表
        List<Class<?>> classes = factory.getClassByAnnotation(Filterable.class);
        for (Class<?> clz : classes) {
            Filterable filtered = clz.getAnnotation(Filterable.class);
            int order = filtered.order();
            filters.add(Pair.with(order, clz));
        }

        //过滤器排序
        filters.sort((a, b) -> {
            if (a.getValue0() > b.getValue0()) {
                return -1;
            } else if (a.getValue0() < b.getValue0()) {
                return 1;
            }
            return 0;
        });

        return filters;
    }

    /**
     * 执行过滤器
     */
    @Override
    public boolean doBefore(HttpRequest request, HttpResponse response) throws Exception {
        //调用链执行
        for (int i = 0; i < filters.size(); i++) {
            if (!filters.get(i).doBefore(request, response)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 执行过滤器
     */
    @Override
    public boolean doAfter(HttpRequest request, HttpResponse response) throws Exception {
        //调用链执行
        for (int i = filters.size() - 1; i >= 0; i--) {
            if (!filters.get(i).doAfter(request, response)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 检测是否可调用
     */
    public boolean filterable() {
        return !filters.isEmpty();
    }

}
