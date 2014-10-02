package com.dominion.common.actions;

import com.dominion.common.ActionPlayerPair;
import com.dominion.common.Player;
import com.dominion.common.PublicCards;
import java.util.List;

public class StaticAction implements Action {

    /**
     * @param actionIncrease
     * @param buyIncrease
     * @param coinIncrease
     */

    private final int actionIncrease;
    private final int buyIncrease;
    private final int coinIncrease;

    public StaticAction(final int actionIncrease, final int buyIncrease, final int coinIncrease) {
        if (actionIncrease < 0 || buyIncrease < 0 || coinIncrease < 0) {
            throw new IllegalArgumentException("numbers in staticAction need to be non-negative");
        }
        this.actionIncrease = actionIncrease;
        this.buyIncrease = buyIncrease;
        this.coinIncrease = coinIncrease;
    }

    @Override
    public List<ActionPlayerPair> apply(Player receiver, PublicCards publicCards) {
        assert null != receiver : "Null receiver in action";
        receiver.actionAllowrance += actionIncrease;
        receiver.buyAllowrance += buyIncrease;
        receiver.coinAllowrance += coinIncrease;
        return null;
    }

}
