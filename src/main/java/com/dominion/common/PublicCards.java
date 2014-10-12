package com.dominion.common;

import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PublicCards {

    @JsonProperty("treasure")
    private List<SingleCardPile> treasureCards;

    @JsonProperty("victory")
    private List<SingleCardPile> victoryCards;

    @JsonProperty("action")
    private List<SingleCardPile> actionCards;

    @JsonProperty("trash")
    private MixedCardPile trashPile;

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

    @JsonProperty("treasure")
    public List<SingleCardPile> getTreasure() {
        return treasureCards;
    }

    @JsonProperty("victory")
    public List<SingleCardPile> getvictory() {
        return victoryCards;
    }

    @JsonProperty("action")
    public List<SingleCardPile> getAction() {
        return actionCards;
    }

    @JsonProperty("trash")
    public MixedCardPile gettrash() {
        return trashPile;
    }

    @JsonProperty("treasure")
    public void setTreasure(List<SingleCardPile> treasureCards) {
        this.treasureCards = treasureCards;
    }

    @JsonProperty("victory")
    public void setvictory(List<SingleCardPile> victoryCards) {
        this.victoryCards = victoryCards;
    }

    @JsonProperty("action")
    public void setAction(List<SingleCardPile> actionCards) {
        this.actionCards = actionCards;
    }

    @JsonProperty("trash")
    public void settrash(MixedCardPile trashPile) {
        this.trashPile = trashPile;
    }
}
