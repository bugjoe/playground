package test;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;

/**
 * @author josef.bauer@gi-de.com
 */
public class HttpChannelInitializer extends ChannelInitializer<NioSocketChannel> {
	@Override
	protected void initChannel(NioSocketChannel nioSocketChannel) throws Exception {
		final ChannelPipeline pipeline = nioSocketChannel.pipeline();

		pipeline.addLast(new HttpRequestDecoder());
		pipeline.addLast(new HttpResponseEncoder());
		pipeline.addLast(new MyNettyHandler());
	}
}
