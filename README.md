# Netstrap

#### 项目介绍
基于Spring Boot 和 Netty 实现的WEB开发框架，使用XML配置文件整合Spring模块，去除Starter依赖，完整实现了一套生产可用的WEB容器。 


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

http://localhost:9000/hello       <br/>

```
15:50:32.525 [work-3-2] INFO  io.netstrap.test.filter.LogFilter - GET-/hello
15:50:32.526 [work-3-2] INFO  io.netstrap.test.filter.LogFilter - hello netstrap

```

#### 核心示例

1.Server启动示例

```
@NetstrapApplication
@Log4j2
public class DemoApplication {

    public static void main(String[] args) {
        NetstrapBootApplication.run(DemoApplication.class, args);
    }

}
```

2.Controller示例代码

```
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

}
```

3.Filter示例代码
```
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

4.Config示例代码

```
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

#### 压力测试

环境准备
```
java -jar -server -d64 ./netstrap-test-0.1.jar >> /dev/null &
```

1.Jmeter4
```
Qps(q/s):43344.2
Error:0%
Samples:951623
Min(ms):0
Max(ms):275
Avg(ms):9
Sent(kb/s):7617kb/s
Received(kb/s):4636kb/s
```
2.WRK
```
wrk -c 100 -t 100 -d 60s http://127.0.0.1:9000
Running 1m test @ http://127.0.0.1:9000
  100 threads and 100 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.11ms    2.52ms 205.33ms   95.37%
    Req/Sec   488.09     58.11     1.63k    77.26%
  2908384 requests in 1.00m, 238.53MB read
  Socket errors: connect 0, read 2908335, write 0, timeout 0
  Non-2xx or 3xx responses: 2908384
Requests/sec:  48436.05
Transfer/sec:      3.97MB
```

#### 打包部署

Test默认使用了SpringBoot打包插件，当然也可以使用assembly进行打包。引入打包插件之后，mvn package 就可以打成可执行的jar！
