package io.netstrap.core.netty.initializer;

import io.netstrap.core.config.SslConfig;
import io.netstrap.core.netty.NettyConfig;
import io.netstrap.core.netty.codec.NetstrapRequestDecode;
import io.netstrap.core.netty.codec.NetstrapResponseEncoder;
import io.netstrap.core.netty.handler.DefaultHttpHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.ssl.SslHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLEngine;
import java.io.InputStream;
import java.security.KeyStore;

/**
 * HTTP报文加工管道
 * @author minghu.zhang
 * @date 2018/11/07
 */
@Component
public class HttpChannelInitializer extends ChannelInitializer<SocketChannel> {

    /**
     * HTTP处理器
     */
	private DefaultHttpHandler handler;
    /**
     * 服务配置
     */
	private NettyConfig nettyConfig;

	@Autowired
	public HttpChannelInitializer(DefaultHttpHandler handler,NettyConfig nettyConfig) {
	    this.handler = handler;
	    this.nettyConfig = nettyConfig;
    }
	
	@Override
	public void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline pipeline = ch.pipeline();
        SslConfig ssl = nettyConfig.getSsl();

        if(ssl.isEnable()) {
            initSSL(pipeline,ssl);
        }

		pipeline.addLast(new NetstrapRequestDecode());
		pipeline.addLast(new HttpObjectAggregator(65535));
		pipeline.addLast(handler);
		pipeline.addLast(new NetstrapResponseEncoder());
	}

    /**
     * 初始化SSL
     */
	private void initSSL(ChannelPipeline pipeline, SslConfig ssl) throws Exception {
        KeyStore ks = KeyStore.getInstance("JKS");

        InputStream ksInputStream = HttpChannelInitializer.class.getResourceAsStream(ssl.getJksPath());
        ks.load(ksInputStream, ssl.getJksPwd().toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(ks,ssl.getJksPwd().toCharArray());
        SSLContext sslCtx = SSLContext.getInstance("TLS");
        sslCtx.init(kmf.getKeyManagers(), null, null);

        SSLEngine engine = sslCtx.createSSLEngine();
        engine.setUseClientMode(false);
        engine.setNeedClientAuth(false);
        pipeline.addLast("ssl",new SslHandler(engine));
    }

}
