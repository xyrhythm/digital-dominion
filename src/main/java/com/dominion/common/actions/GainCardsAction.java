package com.dominion.common.actions;

import com.dominion.common.ActionPlayerPair;
import com.dominion.common.Card;
import com.dominion.common.Player;
import com.dominion.common.PublicCards;
import com.dominion.common.evaluators.EligibilityEvaluator;
import java.util.List;

public class GainCardsAction implements Action {

    private final int numCards;
    private final EligibilityEvaluator evaluator;

    public GainCardsAction(final int numCards, final EligibilityEvaluator evaluator) {
        this.numCards = numCards;
        this.evaluator = evaluator;
    }

    @Override
    public List<ActionPlayerPair> apply(Player receiver, PublicCards publicCards) {
        List<Card> cards = receiver.gainCards(numCards, evaluator);
        for (Card card : cards) {
            publicCards.updateCards(card, -1);
        }
        return null;
    }

}
