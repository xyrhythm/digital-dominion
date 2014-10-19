package com.dominion.server;

import com.dominion.common.Game;
import com.dominion.common.GameInfo;
import com.dominion.common.Player;
import java.util.ArrayList;
import java.util.HashMap;

public class ServerStatus {

    private static int gameId = 0;

    private final HashMap<Integer, Game> gameList = new HashMap<>();
    private final HashMap<String, Player> playerList = new HashMap<>();

    public ServerStatus() {

    }

    public int genGameId() {
        gameId++;
        return gameId;
    }

    public void addGame(Game newGame) {
        gameList.put(newGame.getId(), newGame);
    }

    public boolean containsGame(final int gameId) {
        return gameList.containsKey(gameId);
    }

    public void addNewPlayer(Player player) {
        playerList.put(player.name(), player);
    }

    public boolean playerNameInUse(final String name) {
        return playerList.containsKey(name);
    }

    public Game getGame(int gameId) {
        Game game = null;
        if (containsGame(gameId)) {
            game = gameList.get(gameId);
        }
        return game;
    }

    public ArrayList<GameInfo> getGameInfoList() {
        ArrayList<GameInfo> gameInfoList = new ArrayList<GameInfo>();
        for (Game game : gameList.values()) {
            gameInfoList.add(game.getGameInfo());
        }
        return gameInfoList;
    }

}
