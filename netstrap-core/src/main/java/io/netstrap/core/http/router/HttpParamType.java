package io.netstrap.core.http.router;


/**
 * 参数类型
 *
 * @author minghu.zhang
 * @date 2018/11/07
 */
public enum HttpParamType {

    /**
     * URI参数
     */
    BASE_PARAM,
    /**
     * 文件类型
     */
    FILE_PARAM,
    /**
     * 集合类型
     */
    LIST_PARAM,
    /**
     * 数组类型
     */
    ARRAY_PARAM,
    /**
     * 其它类型
     */
    OTHER_PARAM

}
