package com.dominion.common.actions;

import com.dominion.common.Card;
import com.dominion.common.Constants.PlayerAction;
import com.dominion.common.EventMessage;
import com.dominion.common.Player;
import java.util.ArrayList;

public interface Action {

    public ArrayList<EventMessage> apply(Player player);

    public PlayerAction getPlayerAction();

    public boolean isCardEligible(Card card);

}
