package com.dominion.common;

import com.dominion.common.Constants.PlayerSet;
import com.dominion.common.actions.Action;

public class ActionPlayerPair {
    public Action action;
    public PlayerSet playerSet;

    public ActionPlayerPair(final Action action, final PlayerSet playerSet) {
        this.action = action;
        this.playerSet = playerSet;
    }
}
