package com.dominion.utils;

import javax.servlet.Servlet;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.util.thread.QueuedThreadPool;

public class JettyUtils {

    private final static int THREADPOOL_MIN_THREADS = 1;
    private final static int THREADPOOL_MAX_THREADS = 10;

    public static Server createJettyServer(int port) {
        final QueuedThreadPool threadPool = new QueuedThreadPool();
        threadPool.setMaxThreads(THREADPOOL_MAX_THREADS);
        threadPool.setMinThreads(THREADPOOL_MIN_THREADS);
        threadPool.setName("Jetty Thread Pool");
        Server server = new Server(threadPool);
        createConnector(server, port);
        return server;
    }

    private static void createConnector(Server server, final int port) {
        ServerConnector connector = new ServerConnector(server);
        connector.setPort(port);
        server.setConnectors(new Connector[] { connector });
    }

    public static ServletHolder register(final ServletContextHandler root, final Servlet servlet, final String... paths) {
        final ServletHolder s = new ServletHolder(servlet);
        for (String path : paths) {
            root.addServlet(s, "/" + path);
        }
        return s;
    }
}
