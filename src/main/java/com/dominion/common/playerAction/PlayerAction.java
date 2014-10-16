package com.dominion.common.playerAction;

import com.dominion.common.Card;
import com.dominion.common.Constants.Phase;
import com.dominion.common.Player;
import com.dominion.utils.PlayerActionSerializer;
import java.util.List;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(using = PlayerActionSerializer.class)
public interface PlayerAction {

    public String actionName();

    public List<Card> eligibleCards();

    public int numCards();

    public Phase phase();

    public Player receiver();

    public String postUrl();

}
