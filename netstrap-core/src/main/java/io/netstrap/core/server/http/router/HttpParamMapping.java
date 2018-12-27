package io.netstrap.core.server.http.router;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 参数映射
 *
 * @author minghu.zhang
 * @date 2018/12/5 15:12
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpParamMapping {
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
    private HttpContextType contextType;

    /**
     * 参数类型[基本类型，文件类型，集合类型，数组类型]
     */
    private HttpParamType paramType;

    /**
     * 泛型类型[仅支持一个泛型]
     */
    private Class<?> genericType;

    @Override
    public String toString() {
        return "ParamMapping{" +
                "alisName='" + alisName + '\'' +
                ", paramClass=" + paramClass +
                ", contextType=" + contextType +
                ", paramType=" + paramType +
                ", genericType=" + genericType +
                '}';
    }
}
