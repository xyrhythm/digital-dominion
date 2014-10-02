package com.dominion.common.actions;

import com.dominion.common.ActionPlayerPair;
import com.dominion.common.Card;
import com.dominion.common.Card.CardType;
import com.dominion.common.Constants.PlayerSet;
import com.dominion.common.evaluators.EligibilityEvaluator;
import com.dominion.common.evaluators.IncreaseCostEvaluator;
import java.util.ArrayList;
import java.util.List;


public class TrashAndGainIncrementCost extends TrashCardAction {

    private final int gainCardNum;
    private final int incrementCost;
    private final CardType cardType;

    public TrashAndGainIncrementCost(final int numCards, final EligibilityEvaluator evaluator, final int gainCardNum, final int incrementCost, final CardType cardType) {
        super(numCards, evaluator);
        this.gainCardNum = gainCardNum;
        this.incrementCost = incrementCost;
        this.cardType = cardType;
    }

    @Override
    protected List<ActionPlayerPair> setFollowingAction(List<Card> trashCards) {
        Card trashCard = trashCards.get(0);
        EligibilityEvaluator gainCardEvaluator = new IncreaseCostEvaluator(trashCard, incrementCost, cardType);
        List<ActionPlayerPair> followingAction = new ArrayList<ActionPlayerPair>();
        followingAction.add(new ActionPlayerPair(new GainCardsAction(gainCardNum, gainCardEvaluator), PlayerSet.PLAYER_SELF));
        return followingAction;
    }

}
