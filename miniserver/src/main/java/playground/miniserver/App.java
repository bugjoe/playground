package playground.miniserver;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class App {
    private static final AtomicLong COUNTER = new AtomicLong();
    private static final Random RANDOM = new Random(System.currentTimeMillis());
    private static DatagramSocket DATAGRAM_SOCKET;


    public static void main(String... args) throws Exception {

        if (args.length < 3) {
            System.out.println("Need port, number of threads and simulated max. work in ms");
            System.exit(0);
        }

        Runtime.getRuntime().addShutdownHook(new ShutdownActions());

        final int port = Integer.parseInt(args[0]);
        final int threads = Integer.parseInt(args[1]);
        final int work = Integer.parseInt(args[2]);

        System.out.println("Listening on port " + port);
        System.out.println("Max. work " + work + " ms");
        System.out.println("Starting " + threads + " handler threads");

        DATAGRAM_SOCKET = new DatagramSocket(port);

        for (int i = 0; i < threads; i++) {
            new Thread(new Handler(work)).start();
        }
    }

    private static final class ShutdownActions extends Thread {
        @Override
        public void run() {
            super.run();
            DATAGRAM_SOCKET.close();
            System.out.println("Shutdown complete");
        }
    }

    private static final class Handler implements Runnable {
        private final int work;

        public Handler(int work) throws SocketException {
            this.work = work;
        }

        @Override
        public void run() {
            try {
                while (!DATAGRAM_SOCKET.isClosed()) {
                    final DatagramPacket receivePacket = new DatagramPacket(new byte[1024], 1024);
                    DATAGRAM_SOCKET.receive(receivePacket);
                    final InetAddress IPAddress = receivePacket.getAddress();
                    final int port = receivePacket.getPort();
                    final byte[] sendData = "Hey there".getBytes();
                    final long currentCount = COUNTER.incrementAndGet();
                    Thread.sleep(RANDOM.nextInt(work));
                    final DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
                    DATAGRAM_SOCKET.send(sendPacket);

                    if (currentCount % 1000 == 0) {
                        System.out.println(Thread.currentThread().toString() + " / " + currentCount);
                    }
                }
            } catch (Exception e) {
                System.out.println("ERROR: " + e.getMessage());
            }
        }
    }
}
