package openidconnect;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Enumeration;

public class AuthHandler extends AbstractHandler {
    public void handle(String path,
                       Request request,
                       HttpServletRequest httpServletRequest,
                       HttpServletResponse httpServletResponse) throws IOException, ServletException {

        System.out.printf("%n%nHandler called with path: '%s'%n", path);

        final Enumeration<String> headerNames = httpServletRequest.getHeaderNames();

        while (headerNames.hasMoreElements()) {
            System.out.printf("Header: %s%n", headerNames.nextElement());
        }

        final Enumeration<String> parameterNames = httpServletRequest.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            System.out.printf("Parameter: %s%n", parameterNames.nextElement());
        }

        final PrintWriter writer = httpServletResponse.getWriter();

        writer.write("Hello stranger\n");

        writer.flush();

        writer.close();
    }
}
