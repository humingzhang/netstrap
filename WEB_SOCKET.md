#### WebSocketServer使用说明


#### Server启动示例

```
/**
 * 启动程序
 *
 * @author minghu.zhang
 * @date 2018/11/03
 */
@NetstrapApplication
@Log4j2
@EnableNetstrapServer(protocol = ProtocolType.WEB_SOCKET)
public class OrderApplication {

    public static void main(String[] args) {
        ParserConfig.getGlobalInstance().setAutoTypeSupport(true);
        NetstrapBootApplication.run(OrderApplication.class, args);
    }

}
```

#### 控制器示例

请求协议

```
/join/group?a=1&b=2    //请求链接 ，指定访问WebSocketMapping为/join/group的方法
{
    "uid":100          //请求体
}
```
请求方法
```
/**
 * 订阅分组
 * channels.writeAndFlush(new TextWebSocketFrame(json))ØØØØØØØØØØØ
 *
 * @param channel 管道
 * @param param   请求参数
 * @param user    请求报文
 */
@WebSocketMapping("/join/group")
public void joinGroup(Channel channel, Map<String, String> param, User user) {
    String group = param.get("group");
    String token = param.get("token");
    if (Objects.nonNull(group) && Objects.nonNull(user) && Objects.nonNull(token)) {
        orderGroupService.joinGroup(channel, group, user);
    }
}
```
注：若需自定义协议，请参看源码

#### 可选参数类型
```
Channel channel             链接参数
Map<String, String> param   URI参数
User user                   请求体 

```

#### 监听器
```
channel的active和inactive事件框架会通知监听者，如下所示，该监听器必须实现ChannelInactiveListener并被
@Component修饰，框架会基于Spring查找到监听器进行调用：

/**
 * 监听器
 *
 * @author minghu.zhang
 * @date 2018/12/21 16:43
 */
@Component
public class NamedChannelInactiveListener implements ChannelInactiveListener {

    private final UserCacheService userCacheService;
    private final UserGroupService userGroupService;

    @Autowired
    public NamedChannelInactiveListener(UserCacheService userCacheService, UserGroupService userGroupService) {
        this.userCacheService = userCacheService;
        this.userGroupService = userGroupService;
    }

    @Override
    public void channelActive(Channel channel) {
        //
    }

    @Override
    public void channelInactive(Channel channel) {
        //
    }

}

```

#### 打包部署

可以使用SpringBoot打包插件，也可以使用assembly进行打包。引入打包插件后，mvn package 即可生成可执行jar！

#### 应用启动

```
nohup java -jar -server -d64 ./netstrap-test-0.1.jar >> /dev/null &
```