package com.dominion.server;

import com.dominion.common.Constants.Phase;
import com.dominion.common.EventMessage;
import com.dominion.common.Game;
import com.dominion.common.Player;
import com.dominion.utils.JsonUtils;
import java.io.IOException;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketError;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@WebSocket
public class EventWebSocket {

    private final Game game;
    private Session session;
    private final String userName;

    public EventWebSocket(final Game game, final String userName) {
        this.game = game;
        this.userName = userName;
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        System.out.println("Close: statusCode=" + statusCode + ", reason=" + reason);
    }

    @OnWebSocketError
    public void onError(Throwable t) {
        System.out.println("Error: " + t.getMessage());
    }

    @OnWebSocketConnect
    public void onConnect(Session session) {
        this.session = session;
        System.out.println("Connect: " + session.getRemoteAddress().getAddress());
    }

    @OnWebSocketMessage
    public void onMessage(String message) {
        System.out.println("Message: " + message);
        try {
            EventMessage eventMessage = JsonUtils.getMapper().readValue(message, EventMessage.class);
            if (StringUtils.isNotEmpty(eventMessage.getGameStatus())) {
                processGameMessage(eventMessage.getGameStatus());
            }
            if (StringUtils.isNotEmpty(eventMessage.getPhaseName())) {
                processPhaseMessage(eventMessage.getPhaseName());
            }
            if (StringUtils.isNotEmpty(eventMessage.getCardName())) {
                processCardMessage(eventMessage.getCardName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void sendMessage(EventMessage eventMessage) {
        try {
            String message = JsonUtils.writeJsonObjectToString(eventMessage);
            System.out.println("sending message: " + message + " to " + session.getRemoteAddress().getAddress());
            session.getRemote().sendString(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean isActive() {
        return true;
    }

    private void processGameMessage(final String message) {
        if (message.equals("init")) {
            if (game.getNumPlayer() >= 2) {
                initGame();
            } else {
                // alert not enough players to begin
            }
        }
    }

    private void processPhaseMessage(final String message) {
        EventMessage response = new EventMessage();
        response.setPhase(message);
        switch (message) {
        case "none":
            String nextPlayer = game.getNextPlayerName();
            EventWebSocket socket = game.getSocketForUser(nextPlayer);
            socket.sendMessage(new EventMessage("", Phase.ACTION.getDesc(), ""));
            break;
        case "action":
            break;
        case "buy":
            break;
        case "cleanup":
            break;
        default:
            response.setPhase("none");
        }
        sendMessage(response);
    }

    private void processCardMessage(final String message) {

    }

    private void initGame() {
        game.init();
        EventMessage returnMessage = new EventMessage();
        returnMessage.setGameStatus("start");
        returnMessage.setPhase(Phase.ACTION.getDesc());
        for (final Player player : game.getPlayers()) {
            EventWebSocket socket = game.getSocketForUser(player.name());
            if (socket == null) {
                System.out.println("Player " + player.name() + " doesn't have a socket");
            } else {
                socket.sendMessage(returnMessage);
            }
            returnMessage.setPhase(Phase.NONE.getDesc());
        }
    }
}
