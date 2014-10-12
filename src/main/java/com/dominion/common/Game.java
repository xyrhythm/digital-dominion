package com.dominion.common;

import com.dominion.utils.GameUtils;
import com.google.common.base.Preconditions;
import java.util.ArrayList;

public class Game {

    private final int gameId;
    private final Deckset deckset;
    private final Player creator;
    private final ArrayList<Player> playerList;
    private boolean isStarted = false;

    public Game(final int gameId, final Player creator, final Deckset deckSet) {
        Preconditions.checkNotNull(gameId, "gameId cannot be null");
        Preconditions.checkNotNull(creator, "creating player cannot be null");
        Preconditions.checkNotNull(deckSet, "deckset cannot be null");

        this.gameId = gameId;
        this.deckset = deckSet;
        this.creator = creator;
        this.playerList = new ArrayList<Player>();
        playerList.add(creator);
    }

    // Game Setting functions
    public int getId() {
        return gameId;
    }

    public Deckset getDeck() {
        return deckset;
    }

    public Player getCreator() {
        return creator;
    }

    public void addPlayer(Player player) {
        Preconditions.checkNotNull(player, "adding a null player");
        if (!containsPlayer(player.name())) {
            playerList.add(player);
        }
    }

    public int getNumPlayer() {
        return playerList.size();
    }

    public GameInfo getGameInfo() {
        GameInfo info = new GameInfo();
        info.setId(gameId);
        info.setCreator(creator.name());
        info.setDeck(deckset.name());
        ArrayList<String> playerName = new ArrayList<>();
        for (Player player : playerList) {
            playerName.add(player.name());
        }
        info.setPlayers(playerName);
        info.setActive(isStarted);
        return info;
    }

    public boolean containsPlayer(final String name) {
        for (Player p : playerList) {
            if (p.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Player> getPlayers() {
        return playerList;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setActive() {
        this.isStarted = true;
    }

    // Game Play functions
    private PublicCards publicCards;

    public PublicCards getPublicCards() {
        return publicCards;
    }

    public void init() {
        publicCards = GameUtils.initPublicCards(getDeck(), getNumPlayer());
        initPlayerHands();
        // set Player ordering: TODO
    }

    public void play() {

    }

    public void close() {

    }

    private void initPlayerHands() {
        MixedCardPile startingDeck = new MixedCardPile(new ArrayList<Card>());
        for (int i = 0; i < 7; ++i) {
            startingDeck.addOneCard(Card.COPPER);
        }
        for (int i = 0; i < 3; ++i) {
            startingDeck.addOneCard(Card.ESTATE);
        }

        for (Player player : playerList) {
            player.deck = new MixedCardPile(new ArrayList<Card>(startingDeck.cards()));
            publicCards.updateCards(Card.COPPER, -7);
            publicCards.updateCards(Card.ESTATE, -3);
            player.deck.shuffle();
            ArrayList<Card> startingHand = new ArrayList<>();
            for (int i = 0; i < Constants.NORMAL_HAND_SIZE; ++i) {
                startingHand.add(player.deck.drawOneCard());
            }
            player.hand = new MixedCardPile(startingHand);
        }
    }

}
