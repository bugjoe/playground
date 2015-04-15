package test.netty;

import com.google.common.io.BaseEncoding;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

public class MyNettyHandler extends SimpleChannelInboundHandler {
	private final String answer = "Hello stranger";
	private boolean authorized = false;
	private int called = 0;

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object message) throws Exception {
		System.out.println("\n\n>>> Object(" + called++ + "): " + message.getClass());

		if(message instanceof HttpRequest) {
			final HttpRequest httpRequest = (HttpRequest) message;
			final CharSequence authString = httpRequest.headers().get(HttpHeaderNames.AUTHORIZATION);
			if(authString == null) {
				System.out.println("User has to authenticate first");
			} else {
				System.out.println("Authentication string: '" + authString + "'");
				String usernamePasswordBase64 = authString.toString();
				if(usernamePasswordBase64.startsWith("Basic")) {
					usernamePasswordBase64 = usernamePasswordBase64.replace("Basic", "").trim();
				}
				System.out.println("Username:Password Base64: '" + usernamePasswordBase64 + "'");
				final byte[] decodedBytes = BaseEncoding.base64().decode(usernamePasswordBase64);
				final String usernamePassword = new String(decodedBytes);
				System.out.println("User:Password = " + usernamePassword);
				authorized = true;
			}
//			System.out.println("Headers: " + httpRequest.headers());
//			System.out.println("Version: " + httpRequest.protocolVersion());
//
//			final QueryStringDecoder queryStringDecoder = new QueryStringDecoder(httpRequest.uri());
//			System.out.println("URI:     " + queryStringDecoder.uri());
//			System.out.println("Path:    " + queryStringDecoder.path());
//
//			final Map<String, List<String>> parameters = queryStringDecoder.parameters();
//
//			for(String key : parameters.keySet()) {
//				System.out.print(key);
//				System.out.print(" = [");
//
//				for(String value : parameters.get(key)) {
//					System.out.print(value);
//					System.out.print(",");
//				}
//
//				System.out.println("]");
//			}
		}

		if(message instanceof LastHttpContent) {
//			final HttpContent httpContent = (HttpContent) message;
			writeResponse(ctx);
		}

//		ReferenceCountUtil.safeRelease(message);
	}

	private void writeResponse(ChannelHandlerContext ctx) {
		FullHttpResponse response;

		if(authorized) {
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
			response.content().writeBytes(answer.getBytes());
			response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8");
		} else {
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED);
			response.headers().set(HttpHeaderNames.WWW_AUTHENTICATE, "Basic");
		}

		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(response.content().readableBytes()));
		System.out.println("Writing response: " + response);
		ctx.writeAndFlush(response);
	}
}
