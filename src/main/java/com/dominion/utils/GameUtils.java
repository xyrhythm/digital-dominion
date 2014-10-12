package com.dominion.utils;

import com.dominion.common.Card;
import com.dominion.common.Constants;
import com.dominion.common.Deckset;
import com.dominion.common.MixedCardPile;
import com.dominion.common.PublicCards;
import com.dominion.common.SingleCardPile;
import java.util.ArrayList;

public class GameUtils {

    public static PublicCards initPublicCards(final Deckset deck, final int numPlayers) {
        ArrayList<SingleCardPile> treasureCards = new ArrayList<SingleCardPile>();
        for (Card c : deck.treasureCards()) {
            treasureCards.add(new SingleCardPile(c, Constants.PILE_SIZE_UNLIMITED));
        }

        ArrayList<SingleCardPile> victoryCards = new ArrayList<SingleCardPile>();
        for (Card c : deck.victoryCards()) {
            // need to refactor, maybe make it as a card parameter
            if (c.equals(Card.DUCHY) || c.equals(Card.PROVINCE)) {
                victoryCards.add(new SingleCardPile(c, numPlayers == 2 ? Constants.PILE_SIZE_VICTORY_TWO_PLAYER : Constants.PILE_SIZE_VICTORY));
            } else {
                victoryCards.add(new SingleCardPile(Card.ESTATE, Constants.PILE_SIZE_UNLIMITED));
            }
        }

        ArrayList<SingleCardPile> actionCards = new ArrayList<SingleCardPile>();
        for (Card c : deck.actionCards()) {
            actionCards.add(new SingleCardPile(c, Constants.PILE_SIZE));
        }

        MixedCardPile trashCards = new MixedCardPile(new ArrayList<Card>());

        return new PublicCards(treasureCards, victoryCards, actionCards, trashCards);
    }

}
