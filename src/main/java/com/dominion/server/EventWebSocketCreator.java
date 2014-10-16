package com.dominion.server;

import com.dominion.common.Game;
import java.util.regex.Pattern;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;

public class EventWebSocketCreator implements WebSocketCreator {

    private final static Pattern EVENT_GAME = Pattern.compile("/event/[0-9]*");
    private final ServerStatus serverStatus;

    public EventWebSocketCreator(ServerStatus serverStatus) {
        this.serverStatus = serverStatus;
    }

    @Override
    public Object createWebSocket(ServletUpgradeRequest request, ServletUpgradeResponse response) {
        String pathInfo = request.getRequestPath();
        System.out.println("web socket: " + pathInfo);

        EventWebSocket socket = null;
        if (EVENT_GAME.matcher(pathInfo).matches()) {
            final int gameId = Integer.parseInt(pathInfo.split("/")[2]);
            final Game game = serverStatus.getGame(gameId);
            if (game != null) {
                socket = new EventWebSocket(gameId);
                game.setSocket(socket);
            }
        }
        return socket;
    }
}
