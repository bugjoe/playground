package playground.netty;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class App
{
	private static final EventLoopGroup EVENT_LOOP_GROUP = new NioEventLoopGroup();


	public static void main(String[] args) throws Exception
	{
		if (args.length < 2)
		{
			System.out.println("Need following arguments");
			System.out.println("1) port");
			System.out.println("2) max. work time in ms");
			System.exit(0);
		}

		Runtime.getRuntime().addShutdownHook(new ShutdownActions());

		final int port = Integer.parseInt(args[0]);
		final int sleep = Integer.parseInt(args[1]);

		System.out.printf("Listening on port %d%n", port);
		System.out.printf("Max. work time %d ms%n", sleep);

		final Bootstrap bootstrap = new Bootstrap();
		bootstrap.group(EVENT_LOOP_GROUP);
		bootstrap.channel(NioDatagramChannel.class);
		bootstrap.handler(new MyNettyUdpHandler(sleep));
//		bootstrap.bind(port).sync().channel().read().closeFuture().sync();
		final NioDatagramChannel channel = (NioDatagramChannel)bootstrap.bind(port).sync().channel();

		while (channel.isOpen())
		{
			channel.read();
			System.out.println("aha");
		}

		channel.read().closeFuture().sync();
	}

	private static final class ShutdownActions extends Thread
	{
		@Override
		public void run()
		{
			super.run();
			EVENT_LOOP_GROUP.shutdownGracefully();
			System.out.println("Shutdown complete");
		}
	}
}
