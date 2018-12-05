package io.netstrap.core.server.mvc.router;

import io.netstrap.core.server.http.ParamType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参数映射
 * @author minghu.zhang
 * @date 2018/12/5 15:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ParamMapping {
    /**
     * 参数别名
     */
    private String alisName;
    /**
     * 参数值类型
     */
    private Class<?> type;
    /**
     * 参数类型
     */
    private ParamType paramType;
}
