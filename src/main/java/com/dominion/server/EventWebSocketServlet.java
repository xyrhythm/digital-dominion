package com.dominion.server;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class EventWebSocketServlet extends WebSocketServlet {

    private static final long serialVersionUID = -2315055259249849594L;
    private final ServerStatus serverStatus;

    public EventWebSocketServlet(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(600000);
        factory.setCreator(new EventWebSocketCreator(serverStatus));
    }
}
