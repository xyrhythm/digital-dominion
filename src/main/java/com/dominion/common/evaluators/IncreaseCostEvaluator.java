package com.dominion.common.evaluators;

import com.dominion.common.Card;
import com.dominion.common.Card.CardType;

public class IncreaseCostEvaluator implements EligibilityEvaluator {

    private final int incrementCost;
    private final CardType cardType;
    private Card oldCard;

    public IncreaseCostEvaluator(final int incrementCost, final CardType cardType) {
        this.incrementCost = incrementCost;
        this.cardType = cardType;
    }

    @Override
    public boolean isEligible(Card card) {
        assert null != card : "null card in evaluator";
        return card.cost() <= oldCard.cost() + incrementCost && (null == cardType || card.cardType().equals(cardType));
    }

    public void setOldCard(final Card card) {
        this.oldCard = card;
    }
}
