package com.dominion.common;

import java.util.List;

public class Deckset {

    private final String name;
    private final List<Card> actionCards;
    private final List<Card> treasureCards;
    private final List<Card> victoryCards;

    public Deckset(final String name, final List<Card> actionCards, final List<Card> treasureCards,
            final List<Card> victoryCards) {
        this.name = name;
        this.actionCards = actionCards;
        this.treasureCards = treasureCards;
        this.victoryCards = victoryCards;
    }

    public String name() {
        return name;
    }

    public List<Card> actionCards() {
        return actionCards;
    }

    public List<Card> treasureCards() {
        return treasureCards;
    }

    public List<Card> victoryCards() {
        return victoryCards;
    }

}
