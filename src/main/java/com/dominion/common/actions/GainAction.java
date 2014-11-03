package com.dominion.common.actions;

import com.dominion.common.Card;
import com.dominion.common.Constants.PlayerAction;
import com.dominion.common.EventMessage;
import com.dominion.common.Player;
import com.dominion.common.evaluators.EligibilityEvaluator;
import com.dominion.common.evaluators.IncreaseCostEvaluator;
import com.google.common.base.Preconditions;
import java.util.ArrayList;

public class GainAction implements Action {

    private final int numCard;
    private final EligibilityEvaluator evaluator;
    private final boolean needOldCard;
    private final boolean keepInHand;

    private final PlayerAction action = PlayerAction.GAIN;

    public GainAction(final int numCard, final EligibilityEvaluator evaluator, final boolean needOldCard, final boolean keepInHand) {
        this.numCard = numCard;
        this.evaluator = evaluator;
        this.needOldCard = needOldCard;
        this.keepInHand = keepInHand;
    }

    @Override
    public ArrayList<EventMessage> apply(Player player) {
        ArrayList<EventMessage> response = new ArrayList<EventMessage>();
        EventMessage selfResponse = new EventMessage("", player.phase().getDesc(), "");
        EventMessage otherResponse = null;
        selfResponse.setPlayerAction(action.getDesc());
        selfResponse.setReceiver(player);

        player.gainAllowrance = numCard;

        if (needOldCard) {
            Preconditions.checkArgument(evaluator.getClass() == IncreaseCostEvaluator.class);
            ((IncreaseCostEvaluator) evaluator).setOldCard(player.oldCard);
        }
        player.gainEvaluator = evaluator;
        player.keepInHand = keepInHand;

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
