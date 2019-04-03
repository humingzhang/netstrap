package io.netstrap.test.controller;

import com.alibaba.fastjson.JSON;
import io.netstrap.core.http.datagram.HttpRequest;
import io.netstrap.core.http.datagram.HttpResponse;
import io.netstrap.core.http.stereotype.RestController;
import io.netstrap.core.http.stereotype.mapping.GetMapping;
import io.netstrap.core.http.stereotype.mapping.PostMapping;
import io.netstrap.core.http.stereotype.parameter.RequestContext;
import io.netstrap.core.http.stereotype.parameter.RequestForm;
import io.netstrap.test.config.WechatConfig;
import io.netstrap.test.pojo.User;
import io.netty.handler.codec.http.multipart.MixedFileUpload;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

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
    public String hello(@RequestContext String id, User user) {
        return "hello netstrap -> " + id + " -> " + JSON.toJSONString(user);
    }

    /**
     * 打印字符串
     */
    @GetMapping("/hi")
    public String hi(@RequestContext String id) {
        return "hello netstrap -> " + id;
    }

    /**
     * 打印字符串
     */
    @PostMapping("/kalas")
    public String kalas(@RequestForm List<Integer> ka, @RequestForm String[] abc, @RequestForm MixedFileUpload[] def) {
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
