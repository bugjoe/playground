package test;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultHttpRequest;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.util.ReferenceCountUtil;

/**
 * @author josef.bauer@gi-de.com
 */
@ChannelHandler.Sharable
public class MyNettyHandler extends SimpleChannelInboundHandler<Object> {
	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, Object message) throws Exception {
		System.out.println("Object: " + message.getClass());

		if(message instanceof DefaultHttpRequest) {
			final DefaultHttpRequest defaultHttpRequest = (DefaultHttpRequest) message;
			System.out.println("URI: " + defaultHttpRequest.getUri());
		}

		if(message instanceof LastHttpContent) {
			channelHandlerContext.writeAndFlush("It works!");
		}

		ReferenceCountUtil.safeRelease(message);
	}
}
