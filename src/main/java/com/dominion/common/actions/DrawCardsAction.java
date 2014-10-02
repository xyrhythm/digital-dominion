package com.dominion.common.actions;

import com.dominion.common.ActionPlayerPair;
import com.dominion.common.Player;
import com.dominion.common.PublicCards;
import java.util.List;

public class DrawCardsAction implements Action {

    private final int numCards;

    public DrawCardsAction(final int numCards) {
        this.numCards = numCards;
    }

    @Override
    public List<ActionPlayerPair> apply(Player receiver, PublicCards publicCards) {
        assert null != receiver : "Null receiver in action";
        receiver.drawCards(numCards);
        return null;
    }

}
