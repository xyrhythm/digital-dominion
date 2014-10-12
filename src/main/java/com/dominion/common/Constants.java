package com.dominion.common;

import java.util.ArrayList;
import java.util.HashMap;

public class Constants {

    public static final int MIN_PLAYERS = 2;
    public static final int MAX_PLAYERS = 4;
    public static final int NUM_ACTION_CARD = 10;
    public static final int PILE_SIZE_VICTORY_TWO_PLAYER = 8;
    public static final int PILE_SIZE_VICTORY = 12;
    public static final int PILE_SIZE = 10;
    public static final int PILE_SIZE_UNLIMITED = 50;
    public static final int NORMAL_HAND_SIZE = 5;

    public final static String JSON_TYPE = "application/json";

    public enum PlayerSet {
        PLAYER_ALL, PLAYER_SELF, PLAYER_OTHERS, PLAYER_OTHER_ONE, PLAYER_OTHER_SET;
    }

    private static final ArrayList<Card> commonTreasure = new ArrayList<>();
    static {
        commonTreasure.add(Card.COPPER);
        commonTreasure.add(Card.SILVER);
        commonTreasure.add(Card.GOLD);
    }

    private static final ArrayList<Card> commonVictory = new ArrayList<>();
    static {
        commonVictory.add(Card.ESTATE);
        commonVictory.add(Card.DUCHY);
        commonVictory.add(Card.PROVINCE);
    }

    private static final ArrayList<Card> firstGame = new ArrayList<>();
    static {
        firstGame.add(Card.CELLAR);
        firstGame.add(Card.MARKET);
        firstGame.add(Card.MILITIA);
        firstGame.add(Card.MINE);
        firstGame.add(Card.MOAT);
        firstGame.add(Card.REMODLE);
        firstGame.add(Card.SMITHY);
        firstGame.add(Card.VILLAGE);
        firstGame.add(Card.WOODCUTTER);
        firstGame.add(Card.WORKSHOP);
    }

    private static Deckset firstGameDeck = new Deckset("First Game", firstGame, commonTreasure, commonVictory);

    public static HashMap<String, Deckset> popularDeckset = new HashMap<>();
    static {
        popularDeckset.put("First Game", firstGameDeck);
    }

}
