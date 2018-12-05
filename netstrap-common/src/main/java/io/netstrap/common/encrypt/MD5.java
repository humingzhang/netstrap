package io.netstrap.common.encrypt;

import lombok.extern.log4j.Log4j2;

import java.security.MessageDigest;
import java.util.Objects;

/**
 * MD5工具类
 *
 * @author minghu.zhang
 * @date 2018/12/5 11:18
 */
@Log4j2
public class MD5 {

    private static char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6',
            '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 32位
     */
    public static String encrypt(String value) {
        try {
            byte[] btInput = value.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    /**
     * 取中间16位
     */
    public static String encrypt16(String value) {
        return Objects.requireNonNull(encrypt(value)).substring(8, 24);
    }

}
