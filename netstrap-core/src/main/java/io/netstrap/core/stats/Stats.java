package io.netstrap.core.stats;

import lombok.Data;

/**
 * 服务状态
 *
 * @author minghu.zhang
 * @date 2018/11/07
 */
@Data
public class Stats {

    /**
     * 0 ready ,1 start,2 sync,3 stop
     */
    private Code code = Code.READY;

    public enum Code {
        /**
         * 初始化，启动，同步处理，结束
         */
        READY, START, SYNC, STOP
    }

}
