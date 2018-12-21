package io.netstrap.core.server.websocket;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 字符串解析抽象类
 *
 * @author minghu.zhang
 * @date 2018/12/19 11:41
 */
public abstract class AbstractStringDecoder {

    /**
     * 原始文本
     */
    private String text;
    /**
     * 请求地址
     */
    private String uri;
    /**
     * 请求体
     */
    private String body;
    /**
     * 请求参数
     */
    private Map<String,String> param;

    /**
     * 构造函数
     *
     * @param text 构造参数
     */
    public AbstractStringDecoder(String text) {
        this.text = text;
    }

    /**
     * 解析
     *
     * @return this
     */
    public abstract AbstractStringDecoder decode() throws IOException;

    public String uri() {
        return uri;
    }

    public String body() {
        return body;
    }

    public Map<String,String> param() {
        return param;
    }

    protected String text() {
        return text;
    }

    protected void setUri(String uri) {
        this.uri = uri;
    }

    protected void setBody(String body) {
        this.body = body;
    }

    protected void addParam(String key, String value) {
        if(Objects.isNull(param)) {
            param = new HashMap<>();
        }
        param.put(key,value);
    }
}
