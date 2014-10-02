package com.dominion.common.actions;

import com.dominion.common.ActionPlayerPair;
import com.dominion.common.Player;
import com.dominion.common.PublicCards;
import java.util.List;

public interface Action {

    public List<ActionPlayerPair> apply(Player player, PublicCards publicCard);

}
