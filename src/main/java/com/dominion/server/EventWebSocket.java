package com.dominion.server;

import com.dominion.common.Card;
import com.dominion.common.Constants.Phase;
import com.dominion.common.Constants.PlayerAction;
import com.dominion.common.EventMessage;
import com.dominion.common.Game;
import com.dominion.common.Player;
import com.dominion.utils.JsonUtils;
import java.io.IOException;
import java.util.ArrayList;
import org.apache.commons.collections.CollectionUtils;
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
    private final String playerName;
    private final Player player;

    public EventWebSocket(final Game game, final String playerName) {
        this.game = game;
        this.playerName = playerName;
        this.player = game.getPlayerByName(playerName);
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
            if (StringUtils.isNotEmpty(eventMessage.getPhaseName()) && StringUtils.isEmpty(eventMessage.getPlayResult())) {
                processPhaseMessage(eventMessage.getPhaseName());
            }

            if (!player.underAttack) {
                player.setPlayerAction(PlayerAction.NONE);
            }

            if (StringUtils.isNotEmpty(eventMessage.getPlayerAction())) {
                processPlayerActionMessage(eventMessage.getPlayerAction());
            }
            if (StringUtils.isNotEmpty(eventMessage.getCardName())) {
                processCardMessage(eventMessage.getCardName());
            }
            if (StringUtils.isNotEmpty(eventMessage.getPlayResult())) {
                processPlayResult(eventMessage.getPlayResult());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
            game.cleanup(player);
            String nextPlayer = game.getNextPlayerName();
            response.setCommand("update");
            game.sendMessageToAll(response, true);
            EventWebSocket socket = game.getSocketForUser(nextPlayer);
            socket.sendMessage(new EventMessage("", Phase.ACTION.getDesc(), ""));
            response.setCommand("");
            break;
        case "action":
            player.setPhase(Phase.ACTION);
            response.setLogInfo("player " + playerName + " started Action phase");
            game.sendMessageToAll(response, true);
            break;
        case "buy":
            player.setPhase(Phase.BUY);
            response.setLogInfo("player " + playerName + " finished Action phase");
            game.sendMessageToAll(response, true);
            break;
        case "cleanup":
            player.setPhase(Phase.CLEANUP);
            response.setLogInfo("player " + playerName + " finished Buy phase");
            game.sendMessageToAll(response, true);
            break;
        default:
            response.setPhase("none");
            sendMessage(response);
        }
    }

    private void processPlayerActionMessage(final String message) {
        player.setPlayerAction(PlayerAction.fromString(message));
    }

    private void processCardMessage(final String message) {
        Card card = Card.getCardFromName(message);
        System.out.println(card);
        if (card != null) {
            ArrayList<EventMessage> response = game.playCard(player, card);
            processDoubleResponse(response);
        }
        System.out.println("PlayerAction: " + player.playerAction());
        if ((player.playerAction() == PlayerAction.NONE && game.hasNextAction())
                || (player.playerAction() == PlayerAction.TRASH && player.trashAllowrance == 0)) {
            System.out.println("Get next action");
            ArrayList<EventMessage> response = game.processNextAction();
            processDoubleResponse(response);
        }
    }

    private void processPlayResult(String result) {

        if (result.equals("next")) {
            if (player.underAttack) {
                EventMessage selfResponse = new EventMessage();
                EventMessage otherResponse = new EventMessage();
                System.out.println(player.playerAction() + " " + player.discardCards + " " + player.discardAllowrance);
                if (player.playerAction() == PlayerAction.DISCARD && player.discardCards < player.discardAllowrance) {
                    selfResponse.setLogInfo("Need to discard " + (player.discardAllowrance - player.discardCards) + " more");
                    sendMessage(selfResponse);
                } else if (player.playerAction() == PlayerAction.TRASH && player.trashCards < player.trashAllowrance) {
                    selfResponse.setLogInfo("Need to trash " + (player.trashAllowrance - player.trashCards) + " more");
                    sendMessage(selfResponse);
                } else {
                    player.setPlayerAction(PlayerAction.NONE);
                    player.discardAllowrance = 0;
                    player.discardCards = 0;
                    player.trashAllowrance = 0;
                    player.trashCards = 0;
                    selfResponse.setCommand("clear");
                    String logInfo = "player " + player.name() + " finished responding to attack and went back to sleep.";
                    selfResponse.setLogInfo(logInfo);
                    otherResponse.setType("log");
                    otherResponse.setLogInfo(logInfo);
                    sendMessage(selfResponse);
                    game.sendMessageToAll(otherResponse, false);
                    moveToNextAction();
                }
            } else {
                moveToNextAction();
            }
        }

        if (result.equals("anti")) {
            EventMessage selfResponse = new EventMessage();
            EventMessage otherResponse = new EventMessage();

            if (player.hasAntiAttack()) {
                otherResponse.setLogInfo("player " + player.name() + " revealed antiattack card");
                otherResponse.setType("log");
                game.sendMessageToAll(otherResponse, true);
                selfResponse.setPlayerAction("");
                selfResponse.setCommand("clear");
                sendMessage(selfResponse);
                moveToNextAction();
            } else {
                selfResponse.setPlayResult("You Liar! You don't have antiattack!");
                selfResponse.setCommand("attack");
                selfResponse.setPhase(Phase.NONE.getDesc());
                sendMessage(selfResponse);
            }
        }
    }

    private void initGame() {
        game.init();
        EventMessage returnMessage = new EventMessage();
        returnMessage.setGameStatus("start");
        returnMessage.setPhase(Phase.ACTION.getDesc());
        returnMessage.setCommand("update");
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

    private void processDoubleResponse(ArrayList<EventMessage> response) {
        if (CollectionUtils.isNotEmpty(response)) {
            if (response.size() >= 1 && response.get(0) != null) {
                EventMessage message = response.get(0);
                if (message.getReceiver() == null) {
                    sendMessage(message);
                } else {
                    game.getSocketForUser(message.getReceiver().name()).sendMessage(message);
                }
            }

            if (response.size() >= 2 && response.get(1) != null) {
                EventMessage message = response.get(1);
                if (message.getReceiver() == null) {
                    game.sendMessageToAll(message, false);
                } else {
                    game.getSocketForUser(message.getReceiver().name()).sendMessage(message);
                }
            }
        }
    }

    private void moveToNextAction() {
        Player curPlayer = game.getCurPlayer();
        if (game.hasNextAction()) {
            ArrayList<EventMessage> response = game.processNextAction();
            processDoubleResponse(response);
        } else {
            player.curAction = null;
            EventMessage message = new EventMessage();
            message.setPhase(curPlayer.phase().getDesc());
            message.setCommand("clear");
            game.getSocketForUser(curPlayer.name()).sendMessage(message);
        }
    }
}
