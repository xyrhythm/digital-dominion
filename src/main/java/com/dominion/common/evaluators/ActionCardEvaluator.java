package com.dominion.common.evaluators;

import com.dominion.common.Card;

public class ActionCardEvaluator implements EligibilityEvaluator {

    @Override
    public boolean isEligible(Card card) {
        return card.isAction();
    }

}
