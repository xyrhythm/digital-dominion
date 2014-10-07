package com.dominion.server;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.eclipse.jetty.servlet.DefaultServlet;

public class HomePageServlet extends DefaultServlet {

    private static final long serialVersionUID = 7370266043877784557L;

    @Override
    protected void doGet(final HttpServletRequest request, final HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        response.getWriter().println("<h1>On homepage</h1>");
    }

}
