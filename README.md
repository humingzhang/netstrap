# Netstrap

#### 项目介绍

高性能WebSocket服务框架，支持自定义WebSocket协议，默认提供的协议可以基于SpringMVC进行WebSocket开发。

#### 技术介绍

1、支持SpringBoot打包插件 <br/>
2、支持Spring XML配置文件 <br/>
3、支持类MVC开发模式      <br/> 
4、同时支持WebSocket和HTTP<br/>
5、项目基于Netty4构建     <br/>
6、支持WebSocket过滤器    <br/>

#### Maven引入netstrap

```
<dependency>
    <groupId>io.netstrap</groupId>
    <artifactId>netstrap-core</artifactId>
    <version>${version}</version>
</dependency>
```

#### 开发示例

HTTP 应用请看：[HTTP示例](https://github.com/minghu-zhang/netstrap/blob/master/HTTP.md)  
Websocket应用：[WEB_SOCKET示例](https://github.com/minghu-zhang/netstrap/blob/master/WEB_SOCKET.md)

#### 启动示例

```
15:27:46.229 [main] INFO  io.netstrap.core.server.netty.NettyServer - The server bind IP:0.0.0.0 , PORT:9000
15:27:46.233 [main] INFO  io.netstrap.core.server.NetstrapBootApplication - The network service is starting.
15:27:46.276 [main] INFO  io.netstrap.core.server.context.LogoApplicationListener - Printing logo, wait a moment...

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

15:27:46.277 [main] INFO  io.netstrap.core.server.context.LogoApplicationListener - The server started successfully.
```
