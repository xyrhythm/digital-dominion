package com.dominion.common.actions;

import com.dominion.common.ActionPlayerPair;
import com.dominion.common.Card;
import com.dominion.common.Constants.PlayerSet;
import com.dominion.common.evaluators.EligibilityEvaluator;
import java.util.ArrayList;
import java.util.List;

public class DiscardAndDraw extends DiscardCardsAction {

    public DiscardAndDraw(int numCard, final EligibilityEvaluator evaluator) {
        super(numCard, evaluator);
    }

    @Override
    protected List<ActionPlayerPair> setFollowingAction(List<Card> cards) {
        List<ActionPlayerPair> followingAction = new ArrayList<ActionPlayerPair>();
        ActionPlayerPair pair = new ActionPlayerPair(new DrawCardsAction(cards.size()), PlayerSet.PLAYER_SELF);
        followingAction.add(pair);
        return followingAction;
    }

}
