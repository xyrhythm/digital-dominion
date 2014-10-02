package com.dominion.common.actions;

import com.dominion.common.ActionPlayerPair;
import com.dominion.common.Card;
import com.dominion.common.Player;
import com.dominion.common.PublicCards;
import com.dominion.common.evaluators.EligibilityEvaluator;
import java.util.List;
import javax.management.InvalidApplicationException;

public class DiscardCardsAction implements Action {

    /**
     * @param numCard
     *            -1 means no limit, i.e., up to the size of player's hand,
     *            useful for Cellar for example
     */

    private final int numCard;
    private final EligibilityEvaluator evaluator;

    public DiscardCardsAction(final int numCard, final EligibilityEvaluator evaluator) {
        this.numCard = numCard;
        this.evaluator = evaluator;
    }

    @Override
    public List<ActionPlayerPair> apply(Player receiver, PublicCards publicCards) {
        assert null != receiver : "Null receiver in action";
        List<Card> discardCards = null;
        try {
            discardCards = receiver.discardCards(numCard, evaluator);
        } catch (InvalidApplicationException e) {
            e.printStackTrace();
        }
        List<ActionPlayerPair> followingAction = setFollowingAction(discardCards);
        return followingAction;
    }

    protected List<ActionPlayerPair> setFollowingAction(List<Card> cards) {
        return null;
    }

}
