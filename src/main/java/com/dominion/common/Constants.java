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

    public enum Phase {
        NONE("none"),
        ACTION("action"),
        BUY("buy"),
        CLEANUP("cleanup"),
        ATTACK("attack");

        private final String desc;

        Phase(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        private static HashMap<String, Phase> stringToPhase = new HashMap<String, Phase>();
        static {
            for (Phase phase : Phase.values()) {
                stringToPhase.put(phase.getDesc(), phase);
            }
        }

        public static Phase fromString(String phase) {
            return stringToPhase.get(phase);
        }
    }

    public enum PlayerAction {
        NONE("none"),
        GAIN("gain"),
        DISCARD("discard"),
        WAIT("wait"),
        DRAW("draw"),
        TRASH("trash");

        private final String desc;

        PlayerAction(String desc) {
            this.desc = desc;
        }

        public String getDesc() {
            return desc;
        }

        private static HashMap<String, PlayerAction> stringToAction = new HashMap<String, PlayerAction>();
        static {
            for (PlayerAction action : PlayerAction.values()) {
                stringToAction.put(action.getDesc(), action);
            }
        }

        public static PlayerAction fromString(String action) {
            return stringToAction.get(action);
        }
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
        firstGame.add(Card.REMODEL);
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

    public enum PlayResult {
        SUCCESS, INVALID_MOVE, NO_ACTION;
    }
}
