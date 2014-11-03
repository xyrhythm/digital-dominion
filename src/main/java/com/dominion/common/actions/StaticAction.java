package com.dominion.common.actions;

import com.dominion.common.Card;
import com.dominion.common.Constants.PlayerAction;
import com.dominion.common.EventMessage;
import com.dominion.common.Player;
import java.util.ArrayList;

public class StaticAction implements Action {

    /**
     * @param actionIncrease
     * @param buyIncrease
     * @param coinIncrease
     */

    private final int actionIncrease;
    private final int buyIncrease;
    private final int coinIncrease;
    private final PlayerAction action = PlayerAction.NONE;

    public StaticAction(final int actionIncrease, final int buyIncrease, final int coinIncrease) {
        if (actionIncrease < 0 || buyIncrease < 0 || coinIncrease < 0) {
            throw new IllegalArgumentException("numbers in staticAction need to be non-negative");
        }
        this.actionIncrease = actionIncrease;
        this.buyIncrease = buyIncrease;
        this.coinIncrease = coinIncrease;
    }

    @Override
    public ArrayList<EventMessage> apply(Player player) {
        player.actionAllowrance += actionIncrease;
        player.buyAllowrance += buyIncrease;
        player.coinAllowrance += coinIncrease;
        return null;
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
