# netstrap

#### 项目介绍
简单实现，SpringBoot（李鬼） + Netty , 学习Netty和SpringBoot 的第一选择，当然，若你想用于生产环境，请联系我。。 


#### 使用说明

1. 导入项目文件（多模块）
2. 打开netstrap-test
3. 打开DemoApplication
4. 运行Main函数

```
15:49:16.204 [main] INFO  io.netstrap.core.server.netty.NettyServer - The server bind IP:0.0.0.0 , PORT:9000
15:49:16.344 [main] INFO  io.netstrap.core.context.LogoApplicationListener - 
                                                                                
             ***  **                                                            
             ***  **          **             **                                 
             ***  **          **             **                                 
             **** **  ****  ******   ***** ******   * **    ****  *****         
             ******* **  **   **    **       **     ****   **  *  **  **        
             ** **** ******   **     ***     **     **      ***** **  **        
             ** **** **       **        **   **     **     **  ** **  **        
             **  *** **  **   **    **  **   **     **     ** *** **  **        
             **  ***  ****     ****  *****    ****  **     ****** *****         
                                                                  **            
                                                                  **
```

#### 调用方法

http://localhost:9000/hi
http://localhost:9000/hello
http://localhost:9000/config

```
15:50:23.709 [work-3-1] INFO  io.netstrap.test.filter.LogFilter - GET-/hi
15:50:23.709 [work-3-1] INFO  io.netstrap.test.filter.LogFilter - Method Not Allowed
15:50:32.525 [work-3-2] INFO  io.netstrap.test.filter.LogFilter - GET-/hello
15:50:32.526 [work-3-2] INFO  io.netstrap.test.filter.LogFilter - hello netstrap
15:50:38.316 [work-3-3] INFO  io.netstrap.test.filter.LogFilter - GET-/config
15:50:38.413 [work-3-3] INFO  io.netstrap.test.filter.LogFilter - 

{"accessKey":"1000000000","accessValue":"20000000000","indexes":100,"requestUri":"http://www.forexample.com"}

```

#### 核心示例

1.Controller示例代码

```
/**
 * @Description 控制器示例
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
    public void hello(HttpRequest request, HttpResponse response) {
        response.setBody(HttpBody.wrap("hello netstrap".getBytes()));
    }

    /**
     * 打印字符串
     */
    @PostMapping("/hi")
    public String hi(HttpRequest request, HttpResponse response) {
        return "hi netstrap";
    }

    /**
     * 打印配置对象
     */
    @GetMapping("/config")
    public WechatConfig config(HttpRequest request, HttpResponse response) {
        return config;
    }

}
```

2.Filter示例代码
```
/**
 * @Description 打印Log
 * @author minghu.zhang
 * @date 2018/11/29 14:05
 */
@Filterable
@Log4j2
public class LogFilter implements WebFilter {

    @Override
    public boolean doBefore(HttpRequest request, HttpResponse response) throws Exception {
        log.info(request.getMethod().name()+"-"+request.getUri());
        return true;
    }

    @Override
    public boolean doAfter(HttpRequest request, HttpResponse response) throws Exception {
        log.info(new String(response.getBody().getBytes()));
        response.addHeader("Content-Type","application/json");
        return true;
    }

}

```

3.Config示例代码

```
/**
 * @Description 微信配置
 * @author minghu.zhang
 * @date 2018/11/29 14:07
 */
@Configurable
@Prefix("wechat")
@Data
public class WechatConfig {

    private String accessKey;
    private String accessValue;
    private int    indexes;
    private String requestUri;

}

```

#### 压力测试（Jmeter4）

```
Qps(q/s):17765.931202223765
Error:0%
Samples:1022607
Min(ms):0
Max(ms):9263
Avg(ms):51
Sent(kb/s):3120kb/s
Received(kb/s):600kb/s
```

#### 参与贡献

1. Fork 本项目
2. 新建 Feat_xxx 分支
3. 提交代码
4. 新建 Pull Request
