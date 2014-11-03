package com.dominion.common.actions;

import com.dominion.common.ActionPlayerPair;
import com.dominion.common.Card.CardType;
import com.dominion.common.Constants.PlayerSet;
import com.dominion.common.evaluators.AllPassEvaluator;
import com.dominion.common.evaluators.CardTypeEvaluator;
import com.dominion.common.evaluators.IncreaseCostEvaluator;
import com.dominion.common.evaluators.MaxCostEvaluator;
import java.util.ArrayList;

public final class CardActions {

    public static ArrayList<ActionPlayerPair> cellarAction;
    static {
        cellarAction = new ArrayList<ActionPlayerPair>();
        cellarAction.add(new ActionPlayerPair(new DiscardAction(-1, new AllPassEvaluator()), PlayerSet.PLAYER_SELF));
        cellarAction.add(new ActionPlayerPair(new DrawAction(-1), PlayerSet.PLAYER_SELF));
    }

    public static ArrayList<ActionPlayerPair> villageAction;
    static {
        villageAction = new ArrayList<ActionPlayerPair>();
        villageAction.add(new ActionPlayerPair(new DrawAction(1), PlayerSet.PLAYER_SELF));
    }

    public static ArrayList<ActionPlayerPair> workshopAction;
    static {
        workshopAction = new ArrayList<ActionPlayerPair>();
        workshopAction.add(new ActionPlayerPair(new GainAction(1, new MaxCostEvaluator(4, null), false, false), PlayerSet.PLAYER_SELF));
    }

    public static ArrayList<ActionPlayerPair> remodelAction;
    static {
        remodelAction = new ArrayList<ActionPlayerPair>();
        remodelAction.add(new ActionPlayerPair(new TrashAction(1, new AllPassEvaluator()), PlayerSet.PLAYER_SELF));
        remodelAction.add(new ActionPlayerPair(new GainAction(1, new IncreaseCostEvaluator(2, null), true, false), PlayerSet.PLAYER_SELF));
    }

    public static ArrayList<ActionPlayerPair> marketAction;
    static {
        marketAction = new ArrayList<ActionPlayerPair>();
        marketAction.add(new ActionPlayerPair(new DrawAction(1), PlayerSet.PLAYER_SELF));
    }

    public static ArrayList<ActionPlayerPair> moatAction;
    static {
        moatAction = new ArrayList<ActionPlayerPair>();
        moatAction.add(new ActionPlayerPair(new DrawAction(2), PlayerSet.PLAYER_SELF));
    }

    public static ArrayList<ActionPlayerPair> woodcutterAction;
    static {
        woodcutterAction = new ArrayList<ActionPlayerPair>();
    }

    public static ArrayList<ActionPlayerPair> militiaAction;
    static {
        militiaAction = new ArrayList<ActionPlayerPair>();
        militiaAction.add(new ActionPlayerPair(new WaitAction(-1), PlayerSet.PLAYER_SELF));
        militiaAction.add(new ActionPlayerPair(new DiscardAction(2, new AllPassEvaluator(), true), PlayerSet.PLAYER_OTHERS));
    }

    public static ArrayList<ActionPlayerPair> smithyAction;
    static {
        smithyAction = new ArrayList<ActionPlayerPair>();
        smithyAction.add(new ActionPlayerPair(new DrawAction(3), PlayerSet.PLAYER_SELF));
    }

    public static ArrayList<ActionPlayerPair> mineAction;
    static {
        mineAction = new ArrayList<ActionPlayerPair>();
        mineAction.add(new ActionPlayerPair(new TrashAction(1, new CardTypeEvaluator(CardType.TREASURE)), PlayerSet.PLAYER_SELF));
        mineAction.add(new ActionPlayerPair(new GainAction(1, new IncreaseCostEvaluator(3, CardType.TREASURE), true, true), PlayerSet.PLAYER_SELF));
    }
}

