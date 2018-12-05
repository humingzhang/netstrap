package io.netstrap.core.server.http;


/**
 * 参数类型
 * @author minghu.zhang
 * @date 2018/11/07
 */
public enum ParamType {

    /**
     * URI参数
     */
    REQUEST_PARAM,
    /**
     * 请求头
     */
    REQUEST_HEADER,
    /**
     * 请求上下文
     */
    REQUEST_CONTEXT,
    /**
     * 请求表单
     */
    REQUEST_FORM,
    /**
     * 请求体
     */
    REQUEST_BODY,
    /**
     * 请求属性
     */
    REQUEST_ATTRIBUTE
}
