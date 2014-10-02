package com.dominion.common.evaluators;

import com.dominion.common.Card;
import com.dominion.common.Card.CardType;

public class MaxCostEvaluator implements EligibilityEvaluator {

    private final int maxCost;
    private final CardType cardType;

    public MaxCostEvaluator(final int maxCost, final CardType cardType) {
        this.maxCost = maxCost;
        this.cardType = cardType;
    }

    @Override
    public boolean isEligible(Card card) {
        assert null != card : "null card in evaluator";
        return card.cost() <= maxCost && (null == cardType || card.cardType().equals(cardType));
    }

}
