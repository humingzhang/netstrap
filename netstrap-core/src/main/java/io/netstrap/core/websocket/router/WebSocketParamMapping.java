package io.netstrap.core.websocket.router;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * WebSocket参数映射
 *
 * @author minghu.zhang
 * @date 2018/12/5 15:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class WebSocketParamMapping {
    /**
     * 参数别名
     */
    private String alisName;
    /**
     * 参数值类型
     */
    private Class<?> paramClass;
    /**
     * 参数类型
     */
    private WebSocketContextType contextType;

    @Override
    public String toString() {
        return "ParamMapping{" +
                "alisName='" + alisName + '\'' +
                ", paramClass=" + paramClass +
                ", contextType=" + contextType +
                '}';
    }
}
