#### HttpServer使用说明

```
<dependency>
    <groupId>io.netstrap</groupId>
    <artifactId>netstrap-core</artifactId>
    <version>${version}</version>
</dependency>
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
@RequestAttribute  请求属性值(eg:filter设置)
@RequestContext    请求上下文（uri，ip，id）
@RequestForm       表单值（普通，文件）
@RequestHeader     请求头
@RequestParam      请求URL参数
@RequestBody       请求体（默认Json）

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

<br/>

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

Test默认使用了SpringBoot打包插件，也可以使用assembly进行打包。引入打包插件后，mvn package 即可生成可执行jar！

#### 压力测试

环境准备
```
java -jar -server -d64 ./netstrap-test-0.1.jar >> /dev/null &
```
Jmeter4
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

注意：暂不支持路径参数！