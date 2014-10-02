package com.dominion.common.actions;

import com.dominion.common.ActionPlayerPair;
import com.dominion.common.Card;
import com.dominion.common.Player;
import com.dominion.common.PublicCards;
import com.dominion.common.evaluators.EligibilityEvaluator;
import java.util.List;
import javax.management.InvalidApplicationException;

public class TrashCardAction implements Action {

    private final int numCards;
    final EligibilityEvaluator evaluator;

    public TrashCardAction(final int numCards, final EligibilityEvaluator evaluator) {
        this.numCards = numCards;
        this.evaluator = evaluator;
    }

    @Override
    public List<ActionPlayerPair> apply(Player receiver, PublicCards publicCards) {
        List<Card> trashCards = null;
        try {
            trashCards = receiver.trashCards(numCards, evaluator);
        } catch (InvalidApplicationException e) {
            e.printStackTrace();
        }
        publicCards.addTrashCards(trashCards);
        List<ActionPlayerPair> followingAction = setFollowingAction(trashCards);
        return followingAction;
    }

    protected List<ActionPlayerPair> setFollowingAction(List<Card> cards) {
        return null;
    }

}
