package com.dominion.common.actions;

import com.dominion.common.ActionPlayerPair;
import com.dominion.common.Card.CardType;
import com.dominion.common.Constants.PlayerSet;
import com.dominion.common.evaluators.AllPassEvaluator;
import com.dominion.common.evaluators.CardTypeEvaluator;
import com.dominion.common.evaluators.MaxCostEvaluator;
import java.util.ArrayList;
import java.util.List;

public final class CardActions {

    public static List<ActionPlayerPair> cellarAction;
    static {
        cellarAction = new ArrayList<ActionPlayerPair>();
        cellarAction.add(new ActionPlayerPair(new StaticAction(1, 0, 0), PlayerSet.PLAYER_SELF));
        cellarAction.add(new ActionPlayerPair(new DiscardAndDraw(-1, null), PlayerSet.PLAYER_SELF));
    }

    public static List<ActionPlayerPair> villageAction;
    static {
        villageAction = new ArrayList<ActionPlayerPair>();
        villageAction.add(new ActionPlayerPair(new StaticAction(2, 0, 0), PlayerSet.PLAYER_SELF));
        villageAction.add(new ActionPlayerPair(new DrawCardsAction(1), PlayerSet.PLAYER_SELF));
    }

    public static List<ActionPlayerPair> workshopAction;
    static {
        workshopAction = new ArrayList<ActionPlayerPair>();
        workshopAction.add(new ActionPlayerPair(new GainCardsAction(1, new MaxCostEvaluator(4, null)), PlayerSet.PLAYER_SELF));
    }

    public static List<ActionPlayerPair> remodelAction;
    static {
        remodelAction = new ArrayList<ActionPlayerPair>();
        remodelAction.add(new ActionPlayerPair(new TrashAndGainIncrementCost(1, new AllPassEvaluator(), 1, 2, null), PlayerSet.PLAYER_SELF));
    }

    public static List<ActionPlayerPair> marketAction;
    static {
        marketAction = new ArrayList<ActionPlayerPair>();
        marketAction.add(new ActionPlayerPair(new StaticAction(1, 1, 1), PlayerSet.PLAYER_SELF));
        marketAction.add(new ActionPlayerPair(new DrawCardsAction(1), PlayerSet.PLAYER_SELF));
    }

    public static List<ActionPlayerPair> moatAction;
    static {
        moatAction = new ArrayList<ActionPlayerPair>();
        moatAction.add(new ActionPlayerPair(new DrawCardsAction(2), PlayerSet.PLAYER_SELF));
    }

    public static List<ActionPlayerPair> woodcutterAction;
    static {
        woodcutterAction = new ArrayList<ActionPlayerPair>();
        woodcutterAction.add(new ActionPlayerPair(new StaticAction(0, 1, 2), PlayerSet.PLAYER_SELF));
    }

    public static List<ActionPlayerPair> militiaAction;
    static {
        militiaAction = new ArrayList<ActionPlayerPair>();
        militiaAction.add(new ActionPlayerPair(new StaticAction(0, 0, 2), PlayerSet.PLAYER_SELF));
        militiaAction.add(new ActionPlayerPair(new DiscardCardsAction(2, new AllPassEvaluator()), PlayerSet.PLAYER_OTHERS));
    }

    public static List<ActionPlayerPair> smithyAction;
    static {
        smithyAction = new ArrayList<ActionPlayerPair>();
        smithyAction.add(new ActionPlayerPair(new DrawCardsAction(3), PlayerSet.PLAYER_SELF));
    }

    public static List<ActionPlayerPair> mineAction;
    static {
        mineAction = new ArrayList<ActionPlayerPair>();
        mineAction.add(new ActionPlayerPair(new TrashAndGainIncrementCost(1, new CardTypeEvaluator(CardType.TREASURE),
                1, 3, CardType.TREASURE), PlayerSet.PLAYER_SELF));
    }
}

