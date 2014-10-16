package com.dominion.common;

import com.dominion.common.actions.CardActions;
import com.dominion.utils.CardSerializer;
import java.util.List;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(using = CardSerializer.class)
public enum Card {

    // Treasure cards
    COPPER("Copper", CardType.TREASURE, 0, 1, 0, "/resource/img/copper.jpg", "", null),
    SILVER("Silver", CardType.TREASURE, 3, 2, 0, "/resource/img/silver.jpg", "", null),
    GOLD("Gold", CardType.TREASURE, 6, 3, 0, "/resource/img/gold.jpg", "", null),

    // Victory cards
    ESTATE("estate", CardType.VICTORY, 2, 0, 1, "/resource/img/estate.jpg", "", null),
    DUCHY("duchy", CardType.VICTORY, 5, 0, 3, "/resource/img/duchy.jpg", "", null),
    PROVINCE("province", CardType.VICTORY, 8, 0, 6, "/resource/img/province.jpg", "", null),

    // Action cards
    CELLAR("cellar", CardType.ACTION, 2, 0, 0, "/resource/img/cellar.jpg", "", CardActions.cellarAction),
    VILLAGE("village", CardType.ACTION, 3, 0, 0, "/resource/img/village.jpg", "", CardActions.villageAction),
    WORKSHOP("workshop", CardType.ACTION, 3, 0, 0, "/resource/img/workshop.jpg", "", CardActions.workshopAction),
    REMODLE("remodle", CardType.ACTION, 4, 0, 0, "/resource/img/remodel.jpg", "", CardActions.remodelAction),
    MARKET("market", CardType.ACTION, 5, 0, 0, "/resource/img/market.jpg", "", CardActions.marketAction),
    MOAT("moat", CardType.ACTION_ANTIATTACK, 2, 0, 0, "/resource/img/moat.jpg", "", CardActions.moatAction),
    WOODCUTTER("woodcutter", CardType.ACTION, 3, 0, 0, "/resource/img/woodcutter.jpg", "", CardActions.woodcutterAction),
    MILITIA("militia", CardType.ACTION_ATTACK, 4, 0, 0, "/resource/img/militia.jpg", "", CardActions.militiaAction),
    SMITHY("smithy", CardType.ACTION, 4, 0, 0, "/resource/img/smithy.jpg", "", CardActions.smithyAction),
    MINE("mine", CardType.ACTION, 5, 0, 0, "/resource/img/mine.jpg", "", CardActions.mineAction),
    ;

    public enum CardType {
        TREASURE, VICTORY, ACTION, ACTION_ATTACK, ACTION_ANTIATTACK, CURSE, HYBRID;
    }

    private final String name;
    private final CardType type;
    private final int cost;
    private final int treasurePoint;
    private final int victoryPoint;
    private final String imageFile;
    private final String desc;
    private final List<ActionPlayerPair> actions;

    Card(final String name, final CardType type, final int cost, final int treasurePoint, final int victoryPoint, final String imageFile, final String desc, final List<ActionPlayerPair> actions) {
        this.name = name;
        this.type = type;
        this.cost = cost;
        this.treasurePoint = treasurePoint;
        this.victoryPoint = victoryPoint;
        this.imageFile = imageFile;
        this.desc = desc;
        this.actions = actions;
    }

    public String cardName() {
        return name;
    }

    public CardType cardType() {
        return type;
    }

    public int cost() {
        return cost;
    }

    public int treasurePoint() {
        return treasurePoint;
    }

    public int victoryPoint() {
        return victoryPoint;
    }

    public String image() {
        return imageFile;
    }

    public String desc() {
        return desc;
    }

    public boolean isAction() {
        return cardType() == CardType.ACTION || cardType() == CardType.ACTION_ATTACK
                || cardType() == CardType.ACTION_ANTIATTACK;
    }

    public List<ActionPlayerPair> actions() {
        return actions;
    }

}
