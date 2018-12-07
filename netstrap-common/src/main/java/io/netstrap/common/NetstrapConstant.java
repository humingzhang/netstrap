package io.netstrap.common;

/**
 * 系统常量
 *
 * @author minghu.zhang
 * @date 2018/11/03
 */
public interface NetstrapConstant {

    /**
     * 类扫描器和Spring默认扫描路径
     */
    String[] DEFAULT_SCAN = {"io.netstrap.core", "io.netstrap.config",};

    /**
     * 默认绑定地址
     */
    String ANY_ADDRESS = "0.0.0.0";

}
