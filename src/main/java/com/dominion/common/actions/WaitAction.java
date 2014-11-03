package com.dominion.common.actions;

import com.dominion.common.Card;
import com.dominion.common.Constants.PlayerAction;
import com.dominion.common.EventMessage;
import com.dominion.common.Player;
import java.util.ArrayList;

public class WaitAction implements Action {

    private final int numWait;
    private final PlayerAction action = PlayerAction.WAIT;

    public WaitAction(final int numWait) {
        this.numWait = numWait;
    }

    @Override
    public ArrayList<EventMessage> apply(Player player) {
        player.numWait = numWait;

        ArrayList<EventMessage> response = new ArrayList<EventMessage>();
        EventMessage selfResponse = new EventMessage("", player.phase().getDesc(), "");
        EventMessage otherResponse = null;
        selfResponse.setPlayerAction(action.getDesc());
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
        return true;
    }

}
