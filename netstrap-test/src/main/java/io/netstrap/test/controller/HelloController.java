package io.netstrap.test.controller;

import com.alibaba.fastjson.JSON;
import io.netstrap.core.server.http.datagram.HttpRequest;
import io.netstrap.core.server.http.datagram.HttpResponse;
import io.netstrap.core.server.http.wrapper.HttpBody;
import io.netstrap.core.server.mvc.stereotype.RestController;
import io.netstrap.core.server.mvc.stereotype.mapping.GetMapping;
import io.netstrap.core.server.mvc.stereotype.mapping.PostMapping;
import io.netstrap.core.server.mvc.stereotype.parameter.ContextValue;
import io.netstrap.core.server.mvc.stereotype.parameter.FormValue;
import io.netstrap.core.server.mvc.stereotype.parameter.RequestBody;
import io.netstrap.test.config.WechatConfig;
import io.netstrap.test.pojo.User;
import io.netty.handler.codec.http.multipart.MixedFileUpload;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;

/**
 * 控制器示例
 *
 * @author minghu.zhang
 * @date 2018/11/29 11:01
 */
@RestController
@Log4j2
public class HelloController {

    private final WechatConfig config;

    @Autowired
    public HelloController(WechatConfig config) {
        this.config = config;
    }

    /**
     * 打印字符串
     */
    @GetMapping("/hello")
    public String hello(@ContextValue String id, User user) {
        return "hello netstrap -> " + id + " -> " + JSON.toJSONString(user);
    }

    /**
     * 打印字符串
     */
    @GetMapping("/hi")
    public String hi(@ContextValue String id) {
        return "hello netstrap -> " + id;
    }

    /**
     * 打印字符串
     */
    @PostMapping("/kalas")
    public String kalas(@FormValue List<Integer> ka, @FormValue String[] abc, @FormValue MixedFileUpload[] def) {
        return "hello netstrap";
    }

    /**
     * 打印字符串
     */
    @PostMapping("/kerry")
    public String kerry(int[] ka, Double[] abc, List<String> las) {
        return "hello netstrap";
    }

    /**
     * 打印配置对象
     */
    @GetMapping("/spring")
    public WechatConfig config(HttpRequest request, HttpResponse response) {
        return config;
    }

}
