package com.dominion.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.servlet.DefaultServlet;

public class CreateGameServlet extends DefaultServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/plain");
        response.getWriter().println("<h1>In creating game mode</h1>");
    }

}
