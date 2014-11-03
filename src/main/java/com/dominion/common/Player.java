package com.dominion.common;

import com.dominion.common.Constants.Phase;
import com.dominion.common.Constants.PlayerAction;
import com.dominion.common.actions.Action;
import com.dominion.common.evaluators.EligibilityEvaluator;
import java.util.ArrayList;
import java.util.List;

public class Player {

    private final PlayerInfo info;
    public MixedCardPile hand;
    public MixedCardPile deck;
    public MixedCardPile discardPile;
    public MixedCardPile playArea;
    public int actionAllowrance;
    public int buyAllowrance;
    public int coinAllowrance;
    private Phase phase;
    private PlayerAction action;
    public int round = 0;

    public Player(final PlayerInfo info) {
        this.info = info;
        this.hand = new MixedCardPile(new ArrayList<Card>());
        this.deck = new MixedCardPile(new ArrayList<Card>());
        this.discardPile = new MixedCardPile(new ArrayList<Card>());
        this.playArea = new MixedCardPile(new ArrayList<Card>());
        this.actionAllowrance = 1;
        this.buyAllowrance = 1;
        this.coinAllowrance = 0;
        this.phase = Phase.NONE;
        this.action = PlayerAction.NONE;
    }

    public String name() {
        return info.name();
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

    public boolean hasCard(Card card) {
        return hand.contains(card);
    }

    public boolean hasAntiAttack() {
        return hasCard(Card.MOAT);
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


    public void cleanUpOneRound() {
        discardPile.addCards(hand.cards());
        discardPile.addCards(playArea.cards());
        hand = new MixedCardPile(new ArrayList<Card>());
        playArea = new MixedCardPile(new ArrayList<Card>());
        drawCards(Constants.NORMAL_HAND_SIZE);
        this.actionAllowrance = 1;
        this.buyAllowrance = 1;
        this.coinAllowrance = 0;

        this.discardAllowrance = 0;
        this.discardCards = 0;
        this.discardEvaluator = null;

        this.gainAllowrance = 0;
        this.gainCards = 0;
        this.gainEvaluator = null;
        this.keepInHand = false;

        this.drawAllowrance = 0;

        this.trashAllowrance = 0;
        this.trashCards = 0;
        this.trashEvaluator = null;

        this.oldCard = null;
        this.curAction = null;
        this.playingCard = null;

        this.setPlayerAction(PlayerAction.NONE);
        this.numWait = 0;

        this.round += 1;

        this.phase = Phase.NONE;
        this.underAttack = false;
    }

    public int order() {
        return 0;
    }

    public Phase phase() {
        return phase;
    }

    public void setPhase(Phase phase) {
        this.phase = phase;
    }

    public PlayerAction playerAction() {
        return action;
    }

    public void setPlayerAction(final PlayerAction action) {
        this.action = action;
    }

    public void setActionAllowrance(int i) {
        this.actionAllowrance = i;
    }

    public void setBuyAllowrance(int i) {
        this.buyAllowrance = i;
    }

    public void setCoinAllowrance(int i) {
        this.coinAllowrance = i;
    }

    // wait actoin
    public int numWait = 0;

    // discard action
    public int discardAllowrance = 0;
    public int discardCards = 0;
    public EligibilityEvaluator discardEvaluator;

    // gain action
    public int gainAllowrance = 0;
    public int gainCards = 0;
    public EligibilityEvaluator gainEvaluator;
    public boolean keepInHand = false;

    // draw action
    public int drawAllowrance = 0;

    // trash action
    public int trashAllowrance = 0;
    public int trashCards = 0;
    public EligibilityEvaluator trashEvaluator;

    //
    public Card oldCard = null;

    public Action curAction = null;
    public Card playingCard = null;
    public boolean underAttack = false;

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }

        if (!(that instanceof Player)) {
            return false;
        }

        Player player = (Player) that;
        return player.name().equals(this.name());
    }

    public int victoryPoints() {
        int victoryPoints = 0;
        for (Card card : allCards()) {
            if (card.isVictory()) {
                victoryPoints += card.victoryPoint();
            }
        }
        return victoryPoints;
    }

    private ArrayList<Card> allCards() {
        ArrayList<Card> cards = new ArrayList<Card>();
        cards.addAll(hand.cards());
        cards.addAll(deck.cards());
        cards.addAll(playArea.cards());
        cards.addAll(discardPile.cards());
        return cards;
    }
}
