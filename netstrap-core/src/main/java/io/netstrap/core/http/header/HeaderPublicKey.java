package io.netstrap.core.http.header;

/**
 * 公共头信息
 *
 * @author minghu.zhang
 * @date 2018/11/08
 */
public interface HeaderPublicKey {

    /**
     * 内容类型
     * Content-Type
     */
    String CONTENT_TYPE = "Content-Type";

    /**
     * 连接方式
     */
    String CONNECTION = "Connection";

    /**
     * 请求体长度
     */
    String CONTENT_LENGTH = "Content-Length";
}
