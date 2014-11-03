package com.dominion.common;

import com.dominion.common.actions.CardActions;
import com.dominion.common.actions.StaticAction;
import com.dominion.utils.CardSerializer;
import java.util.ArrayList;
import java.util.HashMap;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(using = CardSerializer.class)
public enum Card {

    // Treasure cards
    COPPER("copper", CardType.TREASURE, 0, 1, 0, "/resource/img/copper.jpg", "", null, null),
    SILVER("silver", CardType.TREASURE, 3, 2, 0, "/resource/img/silver.jpg", "", null, null),
    GOLD("gold", CardType.TREASURE, 6, 3, 0, "/resource/img/gold.jpg", "", null, null),

    // Victory cards
    ESTATE("estate", CardType.VICTORY, 2, 0, 1, "/resource/img/estate.jpg", "", null, null),
    DUCHY("duchy", CardType.VICTORY, 5, 0, 3, "/resource/img/duchy.jpg", "", null, null),
    PROVINCE("province", CardType.VICTORY, 8, 0, 6, "/resource/img/province.jpg", "", null, null),

    // Action cards
    CELLAR("cellar", CardType.ACTION, 2, 0, 0, "/resource/img/cellar.jpg", "", new StaticAction(1, 0, 0), CardActions.cellarAction),
    VILLAGE("village", CardType.ACTION, 3, 0, 0, "/resource/img/village.jpg", "", new StaticAction(2, 0, 0), CardActions.villageAction),
    WORKSHOP("workshop", CardType.ACTION, 3, 0, 0, "/resource/img/workshop.jpg", "", null, CardActions.workshopAction),
    REMODEL("remodel", CardType.ACTION, 4, 0, 0, "/resource/img/remodel.jpg", "", null, CardActions.remodelAction),
    MARKET("market", CardType.ACTION, 5, 0, 0, "/resource/img/market.jpg", "", new StaticAction(1, 1, 1), CardActions.marketAction),
    MOAT("moat", CardType.ACTION_ANTIATTACK, 2, 0, 0, "/resource/img/moat.jpg", "", null, CardActions.moatAction),
    WOODCUTTER("woodcutter", CardType.ACTION, 3, 0, 0, "/resource/img/woodcutter.jpg", "", new StaticAction(0, 1, 2), CardActions.woodcutterAction),
    MILITIA("militia", CardType.ACTION_ATTACK, 4, 0, 0, "/resource/img/militia.jpg", "", new StaticAction(0, 0, 2), CardActions.militiaAction),
    SMITHY("smithy", CardType.ACTION, 4, 0, 0, "/resource/img/smithy.jpg", "", null, CardActions.smithyAction),
    MINE("mine", CardType.ACTION, 5, 0, 0, "/resource/img/mine.jpg", "", null, CardActions.mineAction),
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
    private final StaticAction staticAction;
    private final ArrayList<ActionPlayerPair> otherActions;

    Card(final String name, final CardType type, final int cost, final int treasurePoint, final int victoryPoint,
            final String imageFile, final String desc, final StaticAction staticAction, final ArrayList<ActionPlayerPair> otherActions) {
        this.name = name;
        this.type = type;
        this.cost = cost;
        this.treasurePoint = treasurePoint;
        this.victoryPoint = victoryPoint;
        this.imageFile = imageFile;
        this.desc = desc;
        this.staticAction = staticAction;
        this.otherActions = otherActions;
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

    public boolean isTreasure() {
        return cardType() == CardType.TREASURE;
    }

    public boolean isVictory() {
        return cardType() == CardType.VICTORY;
    }

    public StaticAction staticAction() {
        return staticAction;
    }

    public ArrayList<ActionPlayerPair> actions() {
        return otherActions;
    }

    private static final HashMap<String, Card> nameToCard = new HashMap<String, Card>();
    static {
        for (Card card : Card.values()) {
            nameToCard.put(card.name, card);
        }
    }

    public static Card getCardFromName(final String cardName) {
        return nameToCard.get(cardName.toLowerCase());
    }
}
