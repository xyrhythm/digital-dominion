package com.dominion.common.evaluators;

import com.dominion.common.Card;
import com.dominion.common.Card.CardType;

public class IncreaseCostEvaluator implements EligibilityEvaluator {

    private final Card oldCard;
    private final int incrementCost;
    private final CardType cardType;

    public IncreaseCostEvaluator(final Card oldCard, final int incrementCost, final CardType cardType) {
        this.oldCard = oldCard;
        this.incrementCost = incrementCost;
        this.cardType = cardType;
    }

    @Override
    public boolean isEligible(Card card) {
        assert null != card : "null card in evaluator";
        return card.cost() <= oldCard.cost() + incrementCost && (null == cardType || card.cardType().equals(cardType));
    }

}
