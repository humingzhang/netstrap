package io.netstrap.core.exception;

/**
 * 参数解析异常
 *
 * @author minghu.zhang
 * @date 2018/12/5 17:13
 */
public class ParameterParseException extends RuntimeException {

    public ParameterParseException() {
    }

    public ParameterParseException(String message) {
        super(message);
    }

    public ParameterParseException(String message, Throwable cause) {
        super(message, cause);
    }

    public ParameterParseException(Throwable cause) {
        super(cause);
    }

    public ParameterParseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
