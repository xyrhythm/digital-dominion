package com.dominion.common;

import com.dominion.common.Card.CardType;
import com.dominion.common.evaluators.ActionCardEvaluator;
import com.dominion.common.evaluators.AllPassEvaluator;
import com.dominion.common.evaluators.CardTypeEvaluator;
import com.dominion.common.playerAction.PlayerAction;
import com.dominion.server.EventWebSocket;
import com.dominion.utils.GameUtils;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.apache.commons.collections.CollectionUtils;

public class Game {

    private final int gameId;
    private final Deckset deckset;
    private final Player creator;
    private final ArrayList<Player> playerList;
    private boolean isStarted = false;

    public Game(final int gameId, final Player creator, final Deckset deckSet) {
        Preconditions.checkNotNull(gameId, "gameId cannot be null");
        Preconditions.checkNotNull(creator, "creating player cannot be null");
        Preconditions.checkNotNull(deckSet, "deckset cannot be null");

        this.gameId = gameId;
        this.deckset = deckSet;
        this.creator = creator;
        this.playerList = new ArrayList<Player>();
        playerList.add(creator);
    }

    // Game Setting functions
    public int getId() {
        return gameId;
    }

    public Deckset getDeck() {
        return deckset;
    }

    public Player getCreator() {
        return creator;
    }

    public void addPlayer(Player player) {
        Preconditions.checkNotNull(player, "adding a null player");
        if (!containsPlayer(player.name())) {
            playerList.add(player);
        }
    }

    public int getNumPlayer() {
        return playerList.size();
    }

    public String getNextPlayerName() {
        curPlayerIdx = (curPlayerIdx + 1) % getNumPlayer();
        return playerList.get(curPlayerIdx).name();
    }

    public GameInfo getGameInfo() {
        GameInfo info = new GameInfo();
        info.setId(gameId);
        info.setCreator(creator.name());
        info.setDeck(deckset.name());
        ArrayList<String> playerName = new ArrayList<>();
        for (Player player : playerList) {
            playerName.add(player.name());
        }
        info.setPlayers(playerName);
        info.setActive(isStarted);
        return info;
    }

    public boolean containsPlayer(final String name) {
        for (Player p : playerList) {
            if (p.name().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<Player> getPlayers() {
        return playerList;
    }

    private Player getPlayerById(int idx) {
        return playerList.get(idx % getNumPlayer());
    }

    public Player getPlayerByName(final String usrName) {
        Player player = null;
        for (Player p : playerList) {
            if (p.name().equals(usrName)) {
                player = p;
                break;
            }
        }
        return player;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void setActive() {
        this.isStarted = true;
    }

    // Game Play functions
    private PublicCards publicCards;
    private Queue<ActionPlayerPair> inQueue;
    private Queue<PlayerAction> outQueue;
    private int curPlayerIdx;
    private final HashMap<String, EventWebSocket> sockets = new HashMap<String, EventWebSocket>();

    public PublicCards getPublicCards() {
        return publicCards;
    }

    public PlayerAction getPlayerAction() {
        if (outQueue.size() == 0) {
            return null;
        } else {
            return outQueue.peek();
        }
    }

    public void init() {
        publicCards = GameUtils.initPublicCards(getDeck(), getNumPlayer());
        initPlayerHands();
        // set Player ordering: TODO
        // after ordering, sort the playerList in order
        setActive();

        outQueue = new LinkedList<PlayerAction>();
        curPlayerIdx = 0;
    }

    public void play() throws IOException, InterruptedException {
        while (!isOver()) {
            if (outQueue.size() == 0) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {

            }
        }
    }

    public void close() {

    }

    private void initPlayerHands() {
        MixedCardPile startingDeck = new MixedCardPile(new ArrayList<Card>());
        for (int i = 0; i < 7; ++i) {
            startingDeck.addOneCard(Card.COPPER);
        }
        for (int i = 0; i < 3; ++i) {
            startingDeck.addOneCard(Card.ESTATE);
        }

        for (Player player : playerList) {
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

    private boolean isOver() {
        return publicCards.provinceEmpty() || publicCards.numEmptyPiles() >= 3;
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



    private void playCard(Player player, Card card) {
        player.playOneCard(card);
    }

    public void addSocketForUser(EventWebSocket socket, String userName) {
        System.out.println(socket);
        System.out.println(userName);
        sockets.put(userName, socket);
    }

    public EventWebSocket getSocketForUser(final String userName) {
        return sockets.get(userName);
    }

}

//        if (card.cardType() == CardType.TREASURE) {
//            player.coinAllowrance += card.treasurePoint();
//        } else if (card.isAction()) {
//            List<ActionPlayerPair> actions = card.actions();
//            int idx = player.order();
//
//            while (!CollectionUtils.isEmpty(actions)) {
//                List<ActionPlayerPair> followingActions = new ArrayList<ActionPlayerPair>();
//                for (ActionPlayerPair pair : actions) {
//                    List<ActionPlayerPair> possibleActions = null;
//                    Action currentAction = pair.action;
//                    switch (pair.playerSet) {
//                    case PLAYER_SELF:
//                        possibleActions = currentAction.apply(player, publicCards);
//                        followingActions.addAll(possibleActions);
//                        break;
//                    case PLAYER_ALL:
//                        for (int i = 0; i < numPlayers; ++i) {
//                            Player receiver = playersList.get((idx + i) % numPlayers);
//                            if (card.cardType().equals(CardType.ACTION_ATTACK)) {
//                                // ask for antiattack
//                                if (receiver.antiAttack())
//                                    break;
//                            }
//                            possibleActions = currentAction.apply(receiver, publicCards);
//                            followingActions.addAll(possibleActions);
//                        }
//                        break;
//                    case PLAYER_OTHERS:
//                        for (int i = 1; i < numPlayers; ++i) {
//                            Player receiver = playersList.get((idx + i) % numPlayers);
//                            if (card.cardType().equals(CardType.ACTION_ATTACK)) {
//                                // ask for antiattack
//                                if (receiver.antiAttack())
//                                    break;
//                            }
//                            possibleActions = currentAction.apply(receiver, publicCards);
//                            followingActions.addAll(possibleActions);
//                        }
//                        break;
//                    case PLAYER_OTHER_ONE:
//                        Player receiver = player.chooseOneReceiver();
//                        possibleActions = currentAction.apply(receiver, publicCards);
//                        followingActions.addAll(possibleActions);
//                        break;
//                    default:
//                        break;
//                    }
//                    actions.remove(0);
//                    actions.addAll(followingActions);
//                }
//            }
//        } else {
//            LOG.warn("Invalid card type to play in middle of a game");
//        }
//    }
// }
