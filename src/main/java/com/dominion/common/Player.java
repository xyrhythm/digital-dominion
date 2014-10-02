package com.dominion.common;

import com.dominion.common.actions.Action;
import com.dominion.common.evaluators.EligibilityEvaluator;
import java.util.ArrayList;
import java.util.List;
import javax.management.InvalidApplicationException;

public class Player {

    private final PlayerInfo info;
    public MixedCardPile hand;
    public MixedCardPile deck;
    public MixedCardPile discardPile;
    public MixedCardPile playArea;
    public int actionAllowrance;
    public int buyAllowrance;
    public int coinAllowrance;

    public Player(final PlayerInfo info) {
        this.info = info;
        this.hand = new MixedCardPile(new ArrayList<Card>());
        this.deck = new MixedCardPile(new ArrayList<Card>());
        this.discardPile = new MixedCardPile(new ArrayList<Card>());
        this.playArea = new MixedCardPile(new ArrayList<Card>());
        this.actionAllowrance = 1;
        this.buyAllowrance = 1;
        this.coinAllowrance = 0;
    }

    public String name() {
        return info.name();
    }

    public int order() {
        return info.order();
    }

    public void drawCards(int n) {
        if (n > 0) {
            for (int i = 0; i < n; ++i) {
                if (deck.isEmpty()) {
                    deck = new MixedCardPile(new ArrayList<Card>(discardPile.cards()));
                    deck.shuffle();
                    discardPile = new MixedCardPile(new ArrayList<Card>());
                }
                Card card = deck.drawOneCard();
                ArrayList<Card> newCards = new ArrayList<Card>();
                newCards.add(card);
                hand.addCards(newCards);
            }
        }
    }

    public List<Card> discardCards(final int numCards, final EligibilityEvaluator evaluator) throws InvalidApplicationException {
        List<Card> discardCards = chooseCardsFromHand(numCards, evaluator);
        hand.removeCards(discardCards);
        discardPile.addCards(discardCards);
        return discardCards;
    }



    public List<Card> gainCards(int numCards, final EligibilityEvaluator evaluator) {
        List<Card> newCards = chooseCardsFromPublic(numCards, evaluator);
        hand.addCards(newCards);
        return newCards;
    }

    public List<Card> trashCards(final int numCards, final EligibilityEvaluator evaluator) throws InvalidApplicationException {
        List<Card> trashCards = chooseCardsFromHand(numCards, evaluator);
        hand.removeCards(trashCards);
        return trashCards;
    }

    public boolean hasCard(Card card) {
        return hand.contains(card);
    }

    public boolean revealCard(Card card) {
        return true;
    }

    public boolean antiAttack() {
        return hasCard(Card.MOAT) && revealCard(Card.MOAT);
    }

    public void respondAction(Action action, PublicCards publicCards) {
        if (antiAttack()) {
            // TODO: antiattck processing
        } else {
            action.apply(this, publicCards);
        }
    }

    private List<Card> chooseCardsFromHand(final int numCard, final EligibilityEvaluator evaluator)
            throws InvalidApplicationException {
        if (hand.isEmpty()) {
            throw new InvalidApplicationException("cannot choose a card from an empty hand");
        }
        if (numCard < 0 || numCard > hand.size()) {
            throw new IllegalArgumentException("player has a hand of size " + hand.size() + " but the requried discard card num is " + numCard);
        }
        return null;
    }

    private List<Card> chooseCardsFromPublic(final int numCard, final EligibilityEvaluator evaluator) {
        return null;
    }

    public void playOneCard(Card card) {
        hand.removeOneCard(card);
        playArea.addOneCard(card);
    }

    public Player chooseOneReceiver() {
        return null;
    }

    public List<Card> chooseCardsToPlay(EligibilityEvaluator evaluator, int numCards) {
        return null;
    }

    public boolean skipActionPhase() {
        // TODO Auto-generated method stub
        return false;
    }

    public void cleanUpOneRound() {
        discardPile.addCards(hand.cards());
        discardPile.addCards(playArea.cards());
        hand = null;
        playArea = null;
        drawCards(Constants.NORMAL_HAND_SIZE);
        this.actionAllowrance = 1;
        this.buyAllowrance = 1;
        this.coinAllowrance = 0;
    }

}
