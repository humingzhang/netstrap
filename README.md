# Netstrap

#### 项目介绍
Web development framework based on Spring Boot and Netty. Spring module is integrated with XML configuration file, Starter dependency is removed, and a set of web containers for production is fully realized.

#### 启动示例

```
15:27:46.229 [main] INFO  io.netstrap.core.server.netty.NettyServer - The server bind IP:0.0.0.0 , PORT:9000
15:27:46.233 [main] INFO  io.netstrap.core.server.NetstrapBootApplication - The network service[Netty -> WEB_SOCKET] is starting.
15:27:46.276 [main] INFO  io.netstrap.core.server.context.LogoApplicationListener - Printing logo, wait a moment... ^M

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


[HTTP示例]: (HTTP.md)
[WEB_SOCKET示例]: (WEB_SOCKET.md)
