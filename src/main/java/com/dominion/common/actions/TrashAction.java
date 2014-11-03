package com.dominion.common.actions;

import com.dominion.common.Card;
import com.dominion.common.Constants.PlayerAction;
import com.dominion.common.EventMessage;
import com.dominion.common.Player;
import com.dominion.common.evaluators.EligibilityEvaluator;
import com.google.common.base.Preconditions;
import java.util.ArrayList;

public class TrashAction implements Action {

    private final int numCards;
    final EligibilityEvaluator evaluator;
    private final PlayerAction action = PlayerAction.TRASH;

    public TrashAction(final int numCards, final EligibilityEvaluator evaluator) {
        this.numCards = numCards;
        this.evaluator = evaluator;
    }

    @Override
    public ArrayList<EventMessage> apply(Player player) {
        ArrayList<EventMessage> response = new ArrayList<EventMessage>();
        EventMessage selfResponse = new EventMessage("", player.phase().getDesc(), "");
        EventMessage otherResponse = null;
        selfResponse.setPlayerAction(action.getDesc());
        selfResponse.setReceiver(player);

        if (numCards == -1) {
            player.trashAllowrance = player.hand.size();
        } else {
            Preconditions.checkArgument(numCards >= 0 && numCards <= player.hand.size());
            player.trashAllowrance = numCards;
        }

        player.trashEvaluator = evaluator;

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

}
