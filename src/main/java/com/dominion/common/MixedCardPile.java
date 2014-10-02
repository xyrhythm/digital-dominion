package com.dominion.common;

import java.util.Collections;
import java.util.List;

public class MixedCardPile {
    private final List<Card> cards;

    public MixedCardPile(final List<Card> cards) {
        this.cards = cards;
    }

    public List<Card> cards() {
        return cards;
    }

    public int size() {
        return cards.size();
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public final void shuffle() {
        Collections.shuffle(cards);
    }

    public Card drawOneCard() {
        assert (!cards.isEmpty());
        Card card = cards.get(0);
        cards.remove(0);
        return card;
    }

    public boolean contains(Card card) {
        for (Card c : cards) {
            if (card.equals(c)) {
                return true;
            }
        }
        return false;
    }

    public void removeOneCard(Card card) {
        cards.remove(card);
    }

    public void removeCards(List<Card> oldCards) {
        cards.removeAll(oldCards);
    }

    public void addOneCard(Card newCard) {
        cards.add(newCard);
    }

    public void addCards(List<Card> newCards) {
        cards.addAll(newCards);
    }

}
