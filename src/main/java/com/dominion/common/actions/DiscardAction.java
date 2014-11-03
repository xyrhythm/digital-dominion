package com.dominion.common.actions;

import com.dominion.common.Card;
import com.dominion.common.Constants.PlayerAction;
import com.dominion.common.EventMessage;
import com.dominion.common.Player;
import com.dominion.common.evaluators.EligibilityEvaluator;
import com.google.common.base.Preconditions;
import java.util.ArrayList;

public class DiscardAction implements Action {

    /**
     * @param numCard
     *            -1 means no limit, i.e., up to the size of player's hand,
     *            useful for Cellar for example
     */

    private final int numCard;
    private final EligibilityEvaluator evaluator;
    private boolean isAttack = false;
    private final PlayerAction action = PlayerAction.DISCARD;

    public DiscardAction(final int numCard, final EligibilityEvaluator evaluator) {
        this.numCard = numCard;
        this.evaluator = evaluator;
    }

    public DiscardAction(final int numCard, final EligibilityEvaluator evaluator, final boolean isAttack) {
        this.numCard = numCard;
        this.evaluator = evaluator;
        this.isAttack = isAttack;
    }

    @Override
    public ArrayList<EventMessage> apply(Player player) {
        ArrayList<EventMessage> response = new ArrayList<EventMessage>();
        EventMessage selfResponse = new EventMessage("", player.phase().getDesc(), "");
        if (isAttack()) {
            selfResponse.setCommand("attack");
            player.underAttack = true;
        }
        EventMessage otherResponse = null;
        selfResponse.setPlayerAction(action.getDesc());
        selfResponse.setReceiver(player);

        if (numCard == -1) {
            player.discardAllowrance = player.hand.size();
        } else {
            Preconditions.checkArgument(numCard >= 0);
            player.discardAllowrance = Math.min(numCard, player.hand.size());
        }

        player.discardEvaluator = evaluator;

        response.add(selfResponse);
        response.add(otherResponse);
        return response;
    }

    @Override
    public PlayerAction getPlayerAction() {
        return action;
    }

    @Override
    public boolean isCardEligible(Card card) {
        return evaluator == null ? true : evaluator.isEligible(card);
    }

    public boolean isAttack() {
        return isAttack;
    }

}
