package io.netstrap.common.tool;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.sun.xml.internal.ws.api.model.CheckedException;

import java.io.UncheckedIOException;
import java.util.Map;

/**
 * Json转换工具类
 *
 * @author minghu.zhang
 * @date 2018/11/28 17:45
 */
public class JsonTool {

    private JsonTool() {
    }

    public static String obj2json(Object obj) {
        return JSON.toJSONString(obj,
                SerializerFeature.DisableCircularReferenceDetect,
                SerializerFeature.WriteNullStringAsEmpty,
                SerializerFeature.WriteNullNumberAsZero,
                SerializerFeature.WriteNullBooleanAsFalse,
                SerializerFeature.WriteNullListAsEmpty);
    }

    public static <N> N json2obj(String json, Class<?> clz) {
        return (N) JSON.parseObject(json, clz);
    }

    public static Map<String, Object> json2map(String json) {
        return JSON.parseObject(json);
    }
}
