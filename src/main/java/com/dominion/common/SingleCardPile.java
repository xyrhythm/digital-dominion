package com.dominion.common;

public class SingleCardPile {

    private final Card card;
    private int size;

    public SingleCardPile(final Card card, final int size) {
        this.card = card;
        this.size = size;
    }

    public Card card() {
        return card;
    }

    public int size() {
        return size;
    }

    public void changeSize(int i) {
        if (size + i < 0) {
            throw new IllegalArgumentException("the card pile has " + size + " cards, cannot decrease it by " + i);
        }
        size += i;
    }

    public Card drawOneCard() {
        assert size > 1 : "Cannot draw card from an empty pile";
        size--;
        return card;
    }

    public boolean isEmpty() {
        return size <= 0;
    }
}
