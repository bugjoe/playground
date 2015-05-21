package netty;

import com.google.common.io.BaseEncoding;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.*;

public class MyNettyHandler extends SimpleChannelInboundHandler {
	private String username = "";
	private String password = "";
	private boolean authorized = false;
	private int called = 0;

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object message) throws Exception {
		System.out.printf("%n%n>>> Object(%d): %s%n", called++, message.getClass());

		if (message instanceof HttpRequest) {
			final HttpRequest httpRequest = (HttpRequest) message;

			final CharSequence authString = httpRequest.headers().get(HttpHeaderNames.AUTHORIZATION);

			if (authString == null) {
				System.out.println("User has to authenticate first");
			} else {
				System.out.printf("Authentication string: '%s'%n", authString);

				String usernamePasswordBase64 = authString.toString();

				if (usernamePasswordBase64.startsWith("Basic")) {
					usernamePasswordBase64 = usernamePasswordBase64.replace("Basic", "").trim();
				}

				System.out.printf("Username:Password Base64: '%s'%n", usernamePasswordBase64);

				extractUsernameAndPassword(usernamePasswordBase64);

				System.out.printf("Username=%s, Password='%s'%n", username, password);

				authorized = true;
			}
		}

		if (message instanceof LastHttpContent) {
            if (authorized) {
                writeResponse(ctx, String.format("Hello %s (password='%s')%n", username, password));
            } else {
                writeResponse(ctx, "Please authenticate yourself\n");
            }
        }
    }

    private void writeResponse(ChannelHandlerContext ctx, String responseText) {
		FullHttpResponse response;

		if(authorized) {
			response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK);
		} else {
            response = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.UNAUTHORIZED);
            response.headers().set(HttpHeaderNames.WWW_AUTHENTICATE, "Basic");
        }

        response.content().writeBytes(responseText.getBytes());

		response.headers().set(HttpHeaderNames.CONTENT_LENGTH, String.valueOf(response.content().readableBytes()));

		System.out.printf("Writing response: %s%n", response);

		ctx.writeAndFlush(response);
	}

	private void extractUsernameAndPassword(String usernamePasswordBase64) {
		final byte[] decodedBytes = BaseEncoding.base64().decode(usernamePasswordBase64);

		final String[] usernamePassword = new String(decodedBytes).split(":");

		username = usernamePassword[0];

		if (usernamePassword.length > 1) {
			password = usernamePassword[1];
		}
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.printf("Caught exception: %s%n", cause.getClass().toString());
        writeResponse(ctx, "Internal error occurred!\n");
    }
}
