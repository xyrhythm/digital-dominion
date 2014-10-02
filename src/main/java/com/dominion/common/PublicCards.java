package com.dominion.common;

import java.util.List;

public class PublicCards {

    private final List<SingleCardPile> treasureCards;
    private final List<SingleCardPile> victoryCards;
    private final List<SingleCardPile> actionCards;
    private final MixedCardPile trashPile;

    public PublicCards(final List<SingleCardPile> treasureCards, List<SingleCardPile> victoryCards, List<SingleCardPile> actionCards, MixedCardPile trashPile) {
        this.treasureCards = treasureCards;
        this.victoryCards = victoryCards;
        this.actionCards = actionCards;
        this.trashPile = trashPile;
    }

    public int numEmptyPiles() {
        int res = 0;
        for (SingleCardPile pile : treasureCards) {
            if (pile.isEmpty()) {
                res++;
            }
        }
        for (SingleCardPile pile : victoryCards) {
            if (pile.isEmpty()) {
                res++;
            }
        }
        for (SingleCardPile pile : actionCards) {
            if (pile.isEmpty()) {
                res++;
            }
        }
        return res;
    }

    public void updateCards(Card card, int i) {
        switch (card.cardType()) {
        case TREASURE:
            updateCardList(treasureCards, card, i);
            break;
        case VICTORY:
            updateCardList(victoryCards, card, i);
            break;
        case ACTION:
            updateCardList(actionCards, card, i);
        default:
            break;
        }
    }

    private void updateCardList(List<SingleCardPile> cardList, Card card, int i) {
        for (SingleCardPile pile : cardList) {
            if (pile.card() == card) {
                pile.changeSize(i);
                break;
            }
        }
    }

    public boolean provinceEmpty() {
        for (SingleCardPile pile : victoryCards) {
            if (pile.card() == Card.PROVINCE) {
                if (pile.isEmpty()) {
                    return true;
                }
                break;
            }
        }
        return false;
    }

    public void addTrashCards(List<Card> trashCards) {
        this.trashPile.addCards(trashCards);
    }
}
