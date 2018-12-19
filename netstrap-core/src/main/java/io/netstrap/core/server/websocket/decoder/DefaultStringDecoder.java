package io.netstrap.core.server.websocket.decoder;

import io.netstrap.core.server.websocket.AbstractStringDecoder;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;

/**
 * 字符串解析
 *
 * @author minghu.zhang
 * @date 2018/12/19 11:41
 */
public class DefaultStringDecoder extends AbstractStringDecoder {


    /**
     * 构造函数
     *
     * @param text 构造参数
     */
    public DefaultStringDecoder(String text) {
        super(text);
    }

    @Override
    public AbstractStringDecoder decode() throws IOException {

        BufferedReader reader = new BufferedReader(new StringReader(text()));

        String line;
        StringBuilder body = new StringBuilder();
        while ((line = reader.readLine()) != null) {
            if(StringUtils.isEmpty(uri())) {
                setUri(line.trim());
            } else {
                body.append(line);
            }
        }

        setBody(body.toString().trim());

        return this;
    }

}
