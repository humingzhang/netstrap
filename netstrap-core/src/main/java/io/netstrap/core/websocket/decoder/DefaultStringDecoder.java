package io.netstrap.core.websocket.decoder;

import io.netstrap.core.websocket.AbstractStringDecoder;
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
     * 参数分隔符
     */
    private static final String BRK = "?";
    private static final String AND = "&";
    private static final String EQS = "=";
    /**
     * 空串
     */
    private static final String EMPTY = "";

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

        BufferedReader reader = new BufferedReader(new StringReader(text().trim()));

        String url = EMPTY;
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            if (StringUtils.isEmpty(url)) {
                url = line.trim();
            } else {
                body.append(line);
            }
        }

        setBody(body.toString().trim());
        //拆分uri和参数
        decodeUrl(url);

        return this;
    }

    /**
     * 解析url
     * @param url 传入的url参数
     */
    private void decodeUrl(String url) {
        if(url.contains(BRK)) {
            setUri(url.substring(0, url.indexOf(BRK)));
            String paramString = url.substring(url.indexOf(BRK) + 1);
            String[] params;
            if(paramString.contains(AND)) {
                params = paramString.split(AND);
            } else {
                params = new String[]{paramString};
            }
            for (String param : params) {
                if(param.contains(EQS)) {
                    String[] kvs = param.split(EQS);
                    addParam(kvs[0], kvs[1]);
                }
            }
        }
    }

}
