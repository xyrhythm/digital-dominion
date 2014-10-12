package com.dominion.common;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class SingleCardPile {

    @JsonProperty("card")
    private final Card card;

    @JsonProperty("size")
    private int size;

    public SingleCardPile(final Card card, final int size) {
        this.card = card;
        this.size = size;
    }

    @JsonProperty("card")
    public Card card() {
        return card;
    }

    @JsonProperty("size")
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
