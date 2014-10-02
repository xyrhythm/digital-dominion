package com.dominion.common.evaluators;

import com.dominion.common.Card;
import com.dominion.common.Card.CardType;

public class CardTypeEvaluator implements EligibilityEvaluator {

    private final CardType cardType;

    public CardTypeEvaluator(final CardType cardType) {
        this.cardType = cardType;
    }

    @Override
    public boolean isEligible(Card card) {
        return null == cardType || card.cardType().equals(cardType);
    }

}
