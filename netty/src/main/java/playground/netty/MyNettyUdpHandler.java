package playground.netty;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.socket.DatagramPacket;
import io.netty.handler.codec.MessageToMessageDecoder;

import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author josef.bauer@webants.com
 */
public class MyNettyUdpHandler extends MessageToMessageDecoder<DatagramPacket> {
    private static final AtomicLong COUNTER = new AtomicLong();
    private final Random random = new Random(System.currentTimeMillis());
    private final int sleep;

    public MyNettyUdpHandler(int sleep) {
        this.sleep = sleep;
    }

    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, DatagramPacket datagramPacket, List list) throws Exception {
        new Thread(() -> {
            final long currentCount = COUNTER.incrementAndGet();

            try {
                Thread.sleep(random.nextInt(sleep));
            } catch (InterruptedException e) {
                System.out.println("ERROR while sleeping");
            }

            final ByteBuf buffer = Unpooled.buffer(64);
            buffer.writeBytes("Hey there".getBytes());
            channelHandlerContext.channel().writeAndFlush(new DatagramPacket(buffer, datagramPacket.sender()));

            if (currentCount % 1000 == 0) {
                System.out.println(Thread.currentThread().toString() + " / " + currentCount);
            }
        }).start();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.printf("ERROR: %s%n", cause.getMessage());
    }
}
