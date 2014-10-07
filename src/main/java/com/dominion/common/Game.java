package com.dominion.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.Preconditions;
import java.util.ArrayList;

@JsonProperty()
public class Game {

    private final int gameId;
    private final Player creator;
    private final DeckSet deckSet;
    private final ArrayList<Player> playerList;

    public Game(final int gameId, final Player creator, final DeckSet deckSet) {
        Preconditions.checkNotNull(gameId, "gameId cannot be null");
        Preconditions.checkNotNull(creator, "creating player cannot be null");

        this.gameId = gameId;
        this.creator = creator;
        this.deckSet = deckSet;
        playerList = new ArrayList<Player>();
        playerList.add(creator);
    }

    public void addPlayer(Player player) {
        playerList.add(player);
    }
}
