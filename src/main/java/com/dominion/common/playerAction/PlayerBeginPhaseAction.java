package com.dominion.common.playerAction;

import com.dominion.common.Card;
import com.dominion.common.Constants.Phase;
import com.dominion.common.Player;
import java.util.List;

public class PlayerBeginPhaseAction implements PlayerAction {

    private final Phase phase;
    private final Player player;

    public PlayerBeginPhaseAction(final Player player, final Phase phase) {
        this.player = player;
        this.phase = phase;
    }

    @Override
    public String actionName() {
        return "phase_begin";
    }

    @Override
    public List<Card> eligibleCards() {
        return null;
    }

    @Override
    public int numCards() {
        return 0;
    }

    @Override
    public Phase phase() {
        return phase;
    }

    @Override
    public Player receiver() {
        return player;
    }

    @Override
    public String postUrl() {
        return "";
    }

}
