package io.netstrap.core.server.mvc.dispatcher;

import io.netstrap.common.tool.JsonTool;
import io.netstrap.core.server.http.HttpMethod;
import io.netstrap.core.server.http.datagram.HttpRequest;
import io.netstrap.core.server.http.datagram.HttpResponse;
import io.netstrap.core.server.http.wrapper.HttpBody;
import io.netstrap.core.server.mvc.Dispatcher;
import io.netstrap.core.server.mvc.filter.DefaultWebFilter;
import io.netstrap.core.server.mvc.router.RouterFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * 默认Http请求分发
 *
 * @author minghu.zhang
 * @date 2018/11/08
 */
@Component
public class DefaultHttpDispatcher extends Dispatcher {

    /**
     * 构造函数注入
     */
    @Autowired
    public DefaultHttpDispatcher(DefaultWebFilter webFilter) {
        super(webFilter);
    }

    @Override
    protected void dispatcher(HttpRequest request, HttpResponse response) {
        RouterFactory factory = RouterFactory.get();
        String uri = request.getUri();
        RouterFactory.Router router = factory.get(uri);
        if (router.getUri().equals(uri)) {
            //检查method
            HttpMethod requestMethod = request.getMethod();
            boolean methodNotAllowed = true;
            for (HttpMethod method : router.getMethods()) {
                if (method.equals(requestMethod)) {
                    methodNotAllowed = false;
                    break;
                }
            }
            if (methodNotAllowed) {
                router = factory.getMethodNotAllowedRouter();
            }
        }

        doInvoke(router, request, response);
    }

    /**
     * 执行调用
     */
    private void doInvoke(RouterFactory.Router router, HttpRequest request, HttpResponse response) {
        //执行调用
        final Object result;
        try {
            result = router.getAction().invoke(router.getInvoker(), request, response);
            if (Objects.nonNull(result)) {
                byte[] body;
                if(result instanceof String) {
                    body = ((String) result).getBytes();
                } else {
                    body = JsonTool.obj2json(result).getBytes();
                }
                response.setBody(HttpBody.wrap(body));
            }
        } catch (Exception e) {
            e.getCause().printStackTrace();
            doInvoke(RouterFactory.get().getInternalServiceErrorRouter(), request, response);
        }
    }
}
