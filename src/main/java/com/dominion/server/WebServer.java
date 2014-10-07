package com.dominion.server;

import com.dominion.utils.JettyUtils;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ContextHandlerCollection;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class WebServer {

    private final static int PORT = 8080;

    public static void main(String[] args) throws Exception {
        Server server = JettyUtils.createJettyServer(PORT);

        final HandlerCollection handlerCollection = new HandlerCollection();

        // simple handlers
        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase(".");
        handlerCollection.addHandler(resourceHandler);

        final ContextHandlerCollection contexts = new ContextHandlerCollection();
        handlerCollection.addHandler(contexts);

        server.setHandler(handlerCollection);

        // register servlets
        final ServletContextHandler root = new ServletContextHandler(contexts, "/", ServletContextHandler.NO_SECURITY);
        JettyUtils.register(root, new HomePageServlet(), "home");
        JettyUtils.register(root, new CreateGameServlet(), "create");

        server.start();
        server.join();
    }




}
