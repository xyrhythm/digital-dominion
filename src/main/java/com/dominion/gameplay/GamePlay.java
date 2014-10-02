package com.dominion.gameplay;

import com.dominion.common.ActionPlayerPair;
import com.dominion.common.Card;
import com.dominion.common.Card.CardType;
import com.dominion.common.Constants;
import com.dominion.common.MixedCardPile;
import com.dominion.common.Player;
import com.dominion.common.PublicCards;
import com.dominion.common.SingleCardPile;
import com.dominion.common.actions.Action;
import com.dominion.common.evaluators.ActionCardEvaluator;
import com.dominion.common.evaluators.AllPassEvaluator;
import com.dominion.common.evaluators.CardTypeEvaluator;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.log4j.Logger;

public class GamePlay {

    private final static Logger LOG = Logger.getLogger(GamePlay.class);

    private final List<Player> playersList;
    private final List<Card> actionCardsList;
    private final int numPlayers;
    private final int round;

    private PublicCards publicCards;

    public GamePlay(List<Player> players, List<Card> actionCards) {
        if (CollectionUtils.isEmpty(players) || players.size() > Constants.MAX_PLAYERS || players.size() < Constants.MIN_PLAYERS) {
            throw new IllegalArgumentException("This game mode supports 2-4 players.");
        }

        if (CollectionUtils.isEmpty(actionCards) || actionCards.size() != Constants.NUM_ACTION_CARD) {
            throw new IllegalArgumentException("This game mode supports 10 action cards.");
        }

        this.playersList = players;
        this.actionCardsList = actionCards;
        this.numPlayers = players.size();
        this.round = 1;
    }

    public void initGame() {
        // init public cards
        // TODO: refactor game setting
        ArrayList<SingleCardPile> treasureCards = new ArrayList<SingleCardPile>();
        treasureCards.add(new SingleCardPile(Card.COPPER, Constants.PILE_SIZE_UNLIMITED));
        treasureCards.add(new SingleCardPile(Card.SILVER, Constants.PILE_SIZE_UNLIMITED));
        treasureCards.add(new SingleCardPile(Card.GOLD, Constants.PILE_SIZE_UNLIMITED));

        ArrayList<SingleCardPile> victoryCards = new ArrayList<SingleCardPile>();
        treasureCards.add(new SingleCardPile(Card.ESTATE, Constants.PILE_SIZE_UNLIMITED));
        treasureCards.add(new SingleCardPile(Card.DUCHY, numPlayers == 2 ? Constants.PILE_SIZE_VICTORY_TWO_PLAYER : Constants.PILE_SIZE_VICTORY));
        treasureCards.add(new SingleCardPile(Card.PROVINCE, numPlayers == 2 ? Constants.PILE_SIZE_VICTORY_TWO_PLAYER : Constants.PILE_SIZE_VICTORY));

        ArrayList<SingleCardPile> actionCards = new ArrayList<SingleCardPile>();
        for (Card c : actionCardsList) {
            actionCards.add(new SingleCardPile(c, Constants.PILE_SIZE));
        }

        MixedCardPile trashCards = new MixedCardPile(new ArrayList<Card>());

        publicCards = new PublicCards(treasureCards, victoryCards, actionCards, trashCards);

        // init player cards
        MixedCardPile startingDeck = new MixedCardPile(new ArrayList<Card>());
        for (int i = 0; i < 7; ++i) {
            startingDeck.addOneCard(Card.COPPER);
        }
        for (int i = 0; i < 3; ++i) {
            startingDeck.addOneCard(Card.ESTATE);
        }

        for (Player player : playersList) {
            player.deck = new MixedCardPile(new ArrayList<Card>(startingDeck.cards()));
            publicCards.updateCards(Card.COPPER, -7);
            publicCards.updateCards(Card.ESTATE, -3);
            player.deck.shuffle();
            ArrayList<Card> startingHand = new ArrayList<>();
            for (int i = 0; i < Constants.NORMAL_HAND_SIZE; ++i) {
                startingHand.add(player.deck.drawOneCard());
            }
            player.hand = new MixedCardPile(startingHand);
        }
    }

    public void playGame() {

    }

    private void actionPhase(Player player) {
        while (player.actionAllowrance > 0) {
            if (player.skipActionPhase())
                break;
            List<Card> cards = player.chooseCardsToPlay(new ActionCardEvaluator(), 1);
            if (!CollectionUtils.isEmpty(cards)) {
                Card card = cards.get(0);
                playCard(player, card);
            }
            player.actionAllowrance -= 1;
        }
    }

    private void buyPhase(Player player) {
        List<Card> cards = player.chooseCardsToPlay(new CardTypeEvaluator(CardType.TREASURE), -1);
        for (Card card : cards) {
            playCard(player, card);
        }
        player.gainCards(player.buyAllowrance, new AllPassEvaluator());
    }

    private void cleanupPhase(Player player) {
        player.cleanUpOneRound();
    }

    private boolean isOver() {
        return publicCards.provinceEmpty() || publicCards.numEmptyPiles() >= 3;
    }

    private void playCard(Player player, Card card) {
        player.playOneCard(card);

        if (card.cardType() == CardType.TREASURE) {
            player.coinAllowrance += card.treasurePoint();
        } else if (card.isAction()) {
            List<ActionPlayerPair> actions = card.actions();
            int idx = player.order();

            while (!CollectionUtils.isEmpty(actions)) {
                List<ActionPlayerPair> followingActions = new ArrayList<ActionPlayerPair>();
                for (ActionPlayerPair pair : actions) {
                    List<ActionPlayerPair> possibleActions = null;
                    Action currentAction = pair.action;
                    switch (pair.playerSet) {
                    case PLAYER_SELF:
                        possibleActions = currentAction.apply(player, publicCards);
                        followingActions.addAll(possibleActions);
                        break;
                    case PLAYER_ALL:
                        for (int i = 0; i < numPlayers; ++i) {
                            Player receiver = playersList.get((idx + i) % numPlayers);
                            if (card.cardType().equals(CardType.ACTION_ATTACK)) {
                                // ask for antiattack
                                if (receiver.antiAttack()) break;
                            }
                            possibleActions = currentAction.apply(receiver, publicCards);
                            followingActions.addAll(possibleActions);
                        }
                        break;
                    case PLAYER_OTHERS:
                        for (int i = 1; i < numPlayers; ++i) {
                            Player receiver = playersList.get((idx + i) % numPlayers);
                            if (card.cardType().equals(CardType.ACTION_ATTACK)) {
                                // ask for antiattack
                                if (receiver.antiAttack()) break;
                            }
                            possibleActions = currentAction.apply(receiver, publicCards);
                            followingActions.addAll(possibleActions);
                        }
                        break;
                    case PLAYER_OTHER_ONE:
                        Player receiver = player.chooseOneReceiver();
                        possibleActions = currentAction.apply(receiver, publicCards);
                        followingActions.addAll(possibleActions);
                        break;
                    default:
                        break;
                    }
                    actions.remove(0);
                    actions.addAll(followingActions);
                }
            }
        } else {
            LOG.warn("Invalid card type to play in middle of a game");
        }
    }

}
