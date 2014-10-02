package com.dominion.common.evaluators;

import com.dominion.common.Card;

public class AllPassEvaluator implements EligibilityEvaluator {

    @Override
    public boolean isEligible(Card card) {
        assert null != card : "null card in evaluator";
        return true;
    }

}
