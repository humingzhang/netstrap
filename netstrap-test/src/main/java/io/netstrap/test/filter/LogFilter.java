package io.netstrap.test.filter;

import io.netstrap.core.server.http.datagram.AbstractHttpRequest;
import io.netstrap.core.server.http.datagram.AbstractHttpResponse;
import io.netstrap.core.server.http.stereotype.Filterable;
import io.netstrap.core.server.http.WebFilter;
import lombok.extern.log4j.Log4j2;

/**
 * 打印Log
 *
 * @author minghu.zhang
 * @date 2018/11/29 14:05
 */
@Filterable
@Log4j2
public class LogFilter implements WebFilter {

    @Override
    public boolean doBefore(AbstractHttpRequest request, AbstractHttpResponse response) {
        log.info(request.getMethod().name() + "-" + request.getRequestContext().get("uri"));
        return true;
    }

    @Override
    public boolean doAfter(AbstractHttpRequest request, AbstractHttpResponse response) {
        log.info(new String(response.getBody().getBytes()));
        response.addHeader("Content-Type", "application/json");
        return true;
    }

}
