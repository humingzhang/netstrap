package io.netstrap.core.server.http;

/**
 * 默认错误URI
 *
 * @author minghu.zhang
 * @date 2018/12/27 19:38
 */
public interface DefaultErrorUri {

    /**
     * 400
     * 请求参数错误或不能解析
     */
    String BAD_REQUEST = "/ERROR/BAD_REQUEST";

    /**
     * 401
     * 未获授权
     */
    String UNAUTHORIZED = "/ERROR/UNAUTHORIZED";

    /**
     * 403
     * 禁止访问
     */
    String FORBIDDEN = "/ERROR/FORBIDDEN";

    /**
     * 404
     * 路由找不到
     */
    String NOT_FOUND = "/ERROR/NOT_FOUND";

    /**
     * 405
     * HTTP方法不支持
     */
    String METHOD_NOT_ALLOWED = "/ERROR/METHOD_NOT_ALLOWED";

    /**
     * 500
     * 服务器内部错误
     */
    String INTERNAL_SERVICE_ERROR = "/ERROR/INTERNAL_SERVICE_ERROR";

}
