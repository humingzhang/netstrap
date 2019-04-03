package io.netstrap.test.filter;

import io.netstrap.core.http.datagram.HttpRequest;
import io.netstrap.core.http.datagram.HttpResponse;
import io.netstrap.core.context.stereotype.Filterable;
import io.netstrap.core.http.WebFilter;
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
    public boolean doBefore(HttpRequest request, HttpResponse response) {
        log.info(request.getMethod().name() + "-" + request.getRequestContext().get("uri"));
        return true;
    }

    @Override
    public boolean doAfter(HttpRequest request, HttpResponse response) {
        log.info(new String(response.getBody().getBytes()));
        response.addHeader("Content-Type", "application/json");
        return true;
    }

}
