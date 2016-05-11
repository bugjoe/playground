package playground.netty;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

/**
 * @author josef.bauer@webants.com
 */
public class MyNettyUdpHandler extends MessageToMessageDecoder<DatagramPacket>
{
	private static final AtomicLong COUNTER = new AtomicLong();
	private final Random random = new Random(System.currentTimeMillis());
	private final int sleep;

	public MyNettyUdpHandler(int sleep)
	{
		this.sleep = sleep;
	}

	@Override
	protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List list) throws Exception
	{
//        new Thread(() -> {
//            final long currentCount = COUNTER.incrementAndGet();
//
//            try {
//                Thread.sleep(random.nextInt(sleep));
//            } catch (InterruptedException e) {
//                System.out.println("ERROR while sleeping");
//            }
//
//            final ByteBuf buffer = Unpooled.buffer(64);
//            buffer.writeBytes("Hey there".getBytes());
//            channelHandlerContext.channel().writeAndFlush(new DatagramPacket(buffer, datagramPacket.sender()));
//
//            if (currentCount % 1000 == 0) {
//                System.out.println(Thread.currentThread().toString() + " / " + currentCount);
//            }
//        }).start();

		final Channel channel = channelHandlerContext.channel();
		channel.eventLoop().schedule(() -> {
			final ByteBuf buffer = Unpooled.buffer(64);
			buffer.writeBytes("Hey there".getBytes());
			channel.writeAndFlush(new DatagramPacket(buffer, datagramPacket.sender()));
			final long currentCount = COUNTER.incrementAndGet();
			if (currentCount % 1000 == 0)
			{
				System.out.println(Thread.currentThread().toString() + " / " + currentCount);
			}
		}, random.nextInt(sleep), TimeUnit.MILLISECONDS);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception
	{
		System.out.printf("ERROR: %s%n", cause.getMessage());
	}
}
