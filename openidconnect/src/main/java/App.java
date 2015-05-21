package openidconnect;

import org.eclipse.jetty.server.Server;

public class App {
    private static final int port = 8080;
    public static void main(String ... args) throws Exception {
        final Server server = new Server(port);
        server.setHandler(new AuthHandler());
        server.start();
        System.out.printf("Started server and listening on port %d%n", port);
    }
}
