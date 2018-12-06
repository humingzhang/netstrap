# Netstrap

#### 项目介绍
Web development framework based on Spring Boot and Netty. Spring module is integrated with XML configuration file, Starter dependency is removed, and a set of web containers for production is fully realized.


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

2.控制器示例

```
@RestController
@Log4j2
public class HelloController {

    /**
     * 打印字符串
     */
    @GetMapping("/hello")
    public String hello(@ParamValue String username,@HeaderValue("Content-Type") String contentType) {
        return username+":"+password;
    }

}
```

3.可选参数类型
```
@AttributeValue：获取属性值，这个值在整个生命周期有效，可以在filter设置
@ContextValue：  获取请求上下文的值，目前只有，uri，ip，id
@FormValue：     表单值（普通，文件），POST请求有效，目前只支持单个属性，不支持list，不支持数组
@HeaderValue：   请求头里面的值，通过指定name可以获取到请求头的值
@ParamValue      请求URL的参数值，参数不能重复
@RequestBody     请求体，必须为POST请求有效

```

4.Filter示例代码
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

5.默认异常处理
```
参看 DefaultErrorController 代码,自定义以下路径，覆盖即可
/**
 * 400
 * 请求参数错误或不能解析
 */
String BAD_REQUEST = "/ERROR/BAD_REQUEST";

/**
 * 401
 * 未获授权
 */
String UNAUTHORIZED = "/ERROR/UNAUTHORIZED";

/**
 * 403
 * 禁止访问
 */
String FORBIDDEN = "/ERROR/FORBIDDEN";

/**
 * 404
 * 路由找不到
 */
String NOT_FOUND = "/ERROR/NOT_FOUND";

/**
 * 405
 * HTTP方法不支持
 */
String METHOD_NOT_ALLOWED = "/ERROR/METHOD_NOT_ALLOWED";

/**
 * 500
 * 服务器内部错误
 */
String INTERNAL_SERVICE_ERROR = "/ERROR/INTERNAL_SERVICE_ERROR";



```

6.自定义Http状态码
```
参数注入 HttpResponse 即可，举例如下：
/**
 * 打印字符串
 */
@PostMapping("/hi")
public String hi(HttpRequest request, HttpResponse response) {
    response.setStatus(401);
    return "hello netstrap";
}
```

#### 打包部署

Test默认使用了SpringBoot打包插件，当然也可以使用assembly进行打包。引入打包插件之后，mvn package 就可以打成可执行的jar！

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
[root@xtsj ~]# wrk -c 100 -t 100 -d 60s -T 3s http://127.0.0.1:9000/hello
Running 1m test @ http://127.0.0.1:9000/hello
  100 threads and 100 connections
  Thread Stats   Avg      Stdev     Max   +/- Stdev
    Latency     2.02ms    1.21ms  24.36ms   75.37%
    Req/Sec   500.16     56.61     1.18k    75.39%
  2979430 requests in 1.00m, 241.52MB read
  Socket errors: connect 0, read 2979374 此处结果?, write 0, timeout 0
Requests/sec:  49625.03
Transfer/sec:      4.02MB
```

注意：暂不支持链接参数！
