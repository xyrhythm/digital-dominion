package com.dominion.common.actions;

import com.dominion.common.Card;
import com.dominion.common.Constants.PlayerAction;
import com.dominion.common.EventMessage;
import com.dominion.common.Player;
import java.util.ArrayList;

public class DrawAction implements Action {

    private final int numCard;
    private final PlayerAction action = PlayerAction.DRAW;

    // numCard = -1 means drawing discardAllowrance card
    public DrawAction(final int numCard) {
        this.numCard = numCard;
    }

    @Override
    public ArrayList<EventMessage> apply(Player player) {
        ArrayList<EventMessage> response = new ArrayList<EventMessage>();
        EventMessage selfResponse = new EventMessage("", player.phase().getDesc(), "");
        EventMessage otherResponse = new EventMessage();
        selfResponse.setPlayerAction(action.getDesc());
        selfResponse.setReceiver(player);

        if (numCard == -1) {
            player.drawAllowrance = player.discardCards;
        } else {
            player.drawAllowrance = numCard;
        }

        player.discardCards = 0;

        player.drawCards(player.drawAllowrance);
        String logInfo = "player" + player.name() + " drawed " + player.drawAllowrance + " cards";
        selfResponse.setLogInfo(logInfo);
        otherResponse.setLogInfo(logInfo);
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
