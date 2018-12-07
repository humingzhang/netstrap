package io.netstrap.common.tool;

/**
 * 基本类型转换
 *
 * @author minghu.zhang
 * @date 2018/12/5 16:13
 */
public class Convertible {

    /**
     * 判断clz是否可转换
     */
    public static boolean convertible(Class<?> clz) {

        if (clz.equals(Integer.class) || clz.equals(int.class)) {
            return true;
        } else if (clz.equals(Byte.class) || clz.equals(byte.class)) {
            return true;
        } else if (clz.equals(Double.class) || clz.equals(double.class)) {
            return true;
        } else if (clz.equals(Float.class) || clz.equals(float.class)) {
            return true;
        } else if (clz.equals(Character.class) || clz.equals(char.class)) {
            return true;
        } else if (clz.equals(Short.class) || clz.equals(short.class)) {
            return true;
        } else if (clz.equals(Boolean.class) || clz.equals(boolean.class)) {
            return true;
        } else if (clz.equals(String.class)) {
            return true;
        } else {
            return false;
        }
    }

}
