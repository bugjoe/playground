package playground.netty;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;

public class HttpChannelInitializer extends ChannelInitializer<NioSocketChannel> {
	@Override
	protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
		final ChannelPipeline pipeline = nioSocketChannel.pipeline();


		final SelfSignedCertificate ssc = new SelfSignedCertificate("localhost");
		final SslContext sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());

//		pipeline.addLast(sslCtx.newHandler(nioSocketChannel.alloc()));
		pipeline.addLast(new HttpRequestDecoder());
		pipeline.addLast(new HttpResponseEncoder());
		pipeline.addLast(new MyNettyHandler());
	}
}
