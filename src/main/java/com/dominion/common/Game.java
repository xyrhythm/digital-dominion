package com.dominion.common;

import com.dominion.common.Constants.Phase;
import com.dominion.common.Constants.PlayerAction;
import com.dominion.common.actions.Action;
import com.dominion.server.EventWebSocket;
import com.dominion.utils.GameUtils;
import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.HashMap;
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
        Player nextPlayer = playerList.get(curPlayerIdx);
        nextPlayer.setPhase(Phase.ACTION);
        return nextPlayer.name();
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

    public PlayerStatus getCurrentPlayerStatus() {
        final Player player = playerList.get(curPlayerIdx);
        PlayerStatus status = new PlayerStatus();
        status.setCurPlayer(player.name());
        status.setCurPhase(player.phase().getDesc());
        status.setNumAction(player.actionAllowrance);
        status.setNumBuy(player.buyAllowrance);
        status.setNumTreasure(player.coinAllowrance);
        return status;
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
    private int curPlayerIdx;
    private final HashMap<String, EventWebSocket> sockets = new HashMap<String, EventWebSocket>();
    private final HashMap<String, ArrayList<Action>> playerActions = new HashMap<String, ArrayList<Action>>();

    public PublicCards getPublicCards() {
        return publicCards;
    }

    public void init() {
        publicCards = GameUtils.initPublicCards(getDeck(), getNumPlayer());
        initPlayerHands();
        // set Player ordering: TODO
        // after ordering, sort the playerList in order
        setActive();

        curPlayerIdx = 0;
        playerList.get(curPlayerIdx).setPhase(Phase.ACTION);

        // initialize empty playerActions
        for (Player player : getPlayers()) {
            playerActions.put(player.name(), new ArrayList<Action>());
        }
    }

    public void close() {
        // anything needs to clear or save when game is ended
    }

    private boolean isOver() {
        return publicCards.provinceEmpty() || publicCards.numEmptyPiles() >= 3;
    }

    public void cleanup(Player player) {
        player.cleanUpOneRound();
    }

    public ArrayList<EventMessage> playCard(Player player, Card card) {
        System.out.println(player.phase().name());
        ArrayList<EventMessage> response = new ArrayList<EventMessage>();
        EventMessage selfResponse = new EventMessage("", player.phase().getDesc(), "");
        EventMessage otherResponse = null;

        if (player.phase() == Phase.ACTION) {
            if (player.playerAction() == PlayerAction.DISCARD) {
                if (player.discardCards >= player.discardAllowrance) {
                    selfResponse.setPlayResult("You IDIOT! Enough cards discarded; End discarding!");
                } else if (player.curAction != null && !player.curAction.isCardEligible(card)) {
                    selfResponse.setPlayResult("Invalid move! Please check the card description!");
                } else {
                    player.oldCard = card;
                    player.hand.removeOneCard(card);
                    player.discardPile.addOneCard(card);
                    player.discardCards += 1;
                    String logInfo = "player " + player.name() + " discarded card " + card.cardName()
                            + "; num of discarded is " + player.discardCards;
                    selfResponse.setLogInfo(logInfo);
                    selfResponse.setCommand("update");
                    selfResponse.setPlayerAction(PlayerAction.DISCARD.getDesc());
                    otherResponse = new EventMessage();
                    otherResponse.setLogInfo(logInfo);
                }
            } else if (player.playerAction() == PlayerAction.TRASH) {
                if (player.trashCards >= player.trashAllowrance) {
                    selfResponse.setPlayResult("You IDIOT! Enough cards trashed; End trashing!");
                } else if (player.curAction != null && !player.curAction.isCardEligible(card)) {
                    selfResponse.setPlayResult("Invalid move! Please check the card description!");
                } else {
                    player.oldCard = card;
                    player.hand.removeOneCard(card);
                    this.publicCards.addOneTrashCard(card);
                    player.trashCards += 1;
                    String logInfo = "player " + player.name() + " trashed card " + card.cardName();
                    selfResponse.setLogInfo(logInfo);
                    selfResponse.setCommand("update");
                    selfResponse.setPlayerAction(PlayerAction.TRASH.getDesc());
                    otherResponse = new EventMessage();
                    otherResponse.setLogInfo(logInfo);
                }
            } else if (player.playerAction() == PlayerAction.GAIN) {
                if (player.gainCards >= player.gainAllowrance) {
                    selfResponse.setPlayResult("Don't be greedy! Enough cards gained; End gaining!");
                } else if (player.curAction != null && !player.curAction.isCardEligible(card)) {
                    selfResponse.setPlayResult("Invalid move! Please check the card description!");
                } else {
                    boolean isSuccess = false;
                    if (player.playingCard == Card.MINE) {
                        isSuccess = drawCardFromPublic(player, card, true);
                    } else {
                        isSuccess = drawCardFromPublic(player, card, false);
                    }
                    if (isSuccess) {
                        player.gainCards += 1;
                        selfResponse.setPlayResult("Succeefully gained card " + card.cardName());
                        selfResponse.setLogInfo("player " + player.name() + " gained card " + card.cardName());
                        selfResponse.setCommand("update");
                        otherResponse = new EventMessage();
                        otherResponse.setLogInfo("player " + player.name() + " gained card " + card.cardName());
                        otherResponse.setCommand("update");
                        if (isOver()) {
                            sendOverInfo();
                            close();
                        }
                    } else {
                        selfResponse.setPlayResult("Cannot gain card " + card.cardName());
                    }
                }
            } else {
                Preconditions.checkArgument(card.isAction());
                if (player.actionAllowrance > 0) {
                    player.playOneCard(card);
                    player.actionAllowrance -= 1;
                    player.playingCard = card;
                    if (card.staticAction() != null) {
                        card.staticAction().apply(player);
                    }
                    selfResponse.setPlayResult("Succeefully played action card " + card.cardName());
                    selfResponse.setLogInfo("player " + player.name() + " played action card " + card.cardName());
                    selfResponse.setCommand("update");
                    otherResponse = new EventMessage();
                    otherResponse.setLogInfo("player " + player.name() + " played action card " + card.cardName());
                    otherResponse.setCommand("update");
                    populateActions(player, card);
                } else {
                    selfResponse.setPlayResult("No action allowrance, please end action phase");
                }
            }
        }

        if (player.phase() == Phase.BUY) {
            if (player.playerAction() == PlayerAction.GAIN) {
                if (player.coinAllowrance >= card.cost() && player.buyAllowrance > 0) {
                    drawCardFromPublic(player, card, false);
                    player.buyAllowrance -= 1;
                    player.coinAllowrance -= card.cost();
                    selfResponse.setPlayResult("Succeefully gained card " + card.cardName());
                    selfResponse.setLogInfo("player " + player.name() + " gained card " + card.cardName());
                    selfResponse.setCommand("update");
                    otherResponse = new EventMessage();
                    otherResponse.setLogInfo("player " + player.name() + " gained card " + card.cardName());
                    otherResponse.setCommand("update");
                } else if (player.buyAllowrance <= 0) {
                    selfResponse.setPlayResult("No buy allowrance, please end buy phase");
                } else if (player.coinAllowrance < card.cost()) {
                    selfResponse.setPlayResult("Come on! NO bargain please!");
                } else {
                    selfResponse.setPlayResult("Invalid move: u know why");
                }
            } else {
                Preconditions.checkArgument(card.isTreasure());
                player.playOneCard(card);
                player.coinAllowrance += card.treasurePoint();
                System.out.println(player.name() + " played treasure card " + card.cardName());
                selfResponse.setPlayResult("Succeefully played treasure card " + card.cardName());
                selfResponse.setLogInfo("player " + player.name() + " played treasure card " + card.cardName());
                selfResponse.setCommand("update");
                otherResponse = new EventMessage();
                otherResponse.setLogInfo("player " + player.name() + " played treasure card " + card.cardName());
                otherResponse.setCommand("update");
            }
        }

        if (player.phase() == Phase.NONE) {
            if (player.playerAction() == PlayerAction.DISCARD) {
                if (player.discardCards >= player.discardAllowrance) {
                    selfResponse.setPlayResult("You IDIOT! Enough cards discarded; End discarding!");
                } else if (player.curAction != null && !player.curAction.isCardEligible(card)) {
                    selfResponse.setPlayResult("Invalid move! Please check the card description!");
                } else {
                    player.oldCard = card;
                    player.hand.removeOneCard(card);
                    player.discardPile.addOneCard(card);
                    player.discardCards += 1;
                    String logInfo = "player " + player.name() + " discarded card " + card.cardName()
                            + "; num of discarded is " + player.discardCards;
                    selfResponse.setLogInfo(logInfo);
                    selfResponse.setCommand("update");
                    selfResponse.setPlayerAction(PlayerAction.DISCARD.getDesc());
                    otherResponse = new EventMessage();
                    otherResponse.setLogInfo(logInfo);
                }
            }

            if (player.playerAction() == PlayerAction.TRASH) {
                if (player.trashCards >= player.trashAllowrance) {
                    selfResponse.setPlayResult("You IDIOT! Enough cards trashed; End trashing!");
                } else if (player.curAction != null && !player.curAction.isCardEligible(card)) {
                    selfResponse.setPlayResult("Invalid move! Please check the card description!");
                } else {
                    player.oldCard = card;
                    player.hand.removeOneCard(card);
                    this.publicCards.addOneTrashCard(card);
                    player.trashCards += 1;
                    String logInfo = "player " + player.name() + " trashed card " + card.cardName();
                    selfResponse.setLogInfo(logInfo);
                    selfResponse.setCommand("update");
                    selfResponse.setPlayerAction(PlayerAction.TRASH.getDesc());
                    otherResponse = new EventMessage();
                    otherResponse.setLogInfo(logInfo);
                }
            }
        }

        response.add(selfResponse);
        response.add(otherResponse);
        return response;
    }

    private boolean drawCardFromPublic(Player player, Card card, boolean inHand) {
        if (player.playerAction() == PlayerAction.GAIN) {
            if (player.phase() == Phase.BUY) {
                Preconditions.checkArgument(player.coinAllowrance >= card.cost());
            }
            if (publicCards.removeOneCard(card)) {
                if (inHand) {
                    player.hand.addOneCard(card);
                } else {
                    player.discardPile.addOneCard(card);
                }
                return true;
            }
        }
        return false;
    }

    public void addSocketForUser(EventWebSocket socket, String userName) {
        System.out.println(socket);
        System.out.println(userName);
        sockets.put(userName, socket);
    }

    public EventWebSocket getSocketForUser(final String userName) {
        return sockets.get(userName);
    }

    public void sendMessageToAll(EventMessage message, boolean selfIncluded) {
        for (int i = 0; i < getNumPlayer(); ++i) {
            if (i == curPlayerIdx) {
                if (selfIncluded) {
                    getSocketForUser(getPlayerById(i).name()).sendMessage(message);
                }
            } else {
                getSocketForUser(getPlayerById(i).name()).sendMessage(message);
            }
        }
    }

    private void initPlayerHands() {
        MixedCardPile startingDeck = new MixedCardPile(new ArrayList<Card>());
        for (int i = 0; i < 7; ++i) {
            startingDeck.addOneCard(Card.COPPER);
        }
        for (int i = 0; i < 3; ++i) {
            startingDeck.addOneCard(Card.ESTATE);
        }

        // for (Card card : deckset.actionCards()) {
        // // startingDeck.addOneCard(card);
        // startingDeck.addOneCard(Card.MOAT);
        // startingDeck.addOneCard(Card.MILITIA);
        // }

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

    private void populateActions(Player player, Card card) {
        Preconditions.checkArgument(card.isAction());
        ArrayList<ActionPlayerPair> actions = card.actions();
        for (ActionPlayerPair pair : actions) {
            switch (pair.playerSet) {
            case PLAYER_SELF:
                playerActions.get(player.name()).add(pair.action);
                break;
            case PLAYER_ALL:
                for (int i = 0; i < getNumPlayer(); ++i) {
                    Player receiver = playerList.get((curPlayerIdx + i) % getNumPlayer());
                    playerActions.get(receiver.name()).add(pair.action);
                }
                break;
            case PLAYER_OTHERS:
                for (int i = 1; i < getNumPlayer(); ++i) {
                    Player receiver = playerList.get((curPlayerIdx + i) % getNumPlayer());
                    playerActions.get(receiver.name()).add(pair.action);
                }
                break;
            default:
                break;
            }
        }
    }

    public ArrayList<EventMessage> processNextAction() {
        Player curPlayer = playerList.get(curPlayerIdx);
        ArrayList<EventMessage> response = new ArrayList<EventMessage>();
        for (Player player : playerList) {
            for (Action action : playerActions.get(player.name())) {
                System.out.println(player.name() + ": " + action.getPlayerAction().getDesc());
            }
        }

        System.out.println("curPlayer numWait: " + curPlayer.numWait);
        if (curPlayer.numWait == 0) {
            ArrayList<Action> actions = playerActions.get(curPlayer.name());
            if (actions.size() > 0) {
                curPlayer.curAction = actions.get(0);
                response = curPlayer.curAction.apply(curPlayer);
                actions.remove(0);
            }
        } else {
            boolean doneWaiting = true;
            for (Player receiver : playerList) {
                if (!receiver.name().equals(curPlayer.name())) {
                    ArrayList<Action> actions = playerActions.get(receiver.name());
                    if (actions.size() > 0) {
                        receiver.curAction = actions.get(0);
                        System.out.println(receiver.curAction.getPlayerAction().getDesc());
                        response = receiver.curAction.apply(receiver);
                        for (EventMessage message : response) {
                            System.out.println(message == null ? "null" : message.toString());
                        }
                        actions.remove(0);
                        doneWaiting = false;
                        break;
                    }
                }
            }
            if (doneWaiting) {
                curPlayer.numWait = 0;
                response = processNextAction();
            }
        }
        return response;
    }

    public boolean hasNextAction() {
        boolean hasNextAction = false;
        for (Player player : playerList) {
            if (CollectionUtils.isNotEmpty(playerActions.get(player.name()))) {
                hasNextAction = true;
                break;
            }
        }
        return hasNextAction;
    }

    public Player getCurPlayer() {
        return playerList.get(curPlayerIdx);
    }

    public boolean isCurPlayer(Player player) {
        return player.name().equals(getCurPlayer().name());
    }

    private void sendOverInfo() {
        EventMessage message = new EventMessage();
        message.setCommand("finish");
        message.setType("log");
        String winInfo = "Game is Over.\n";
        for (Player player : playerList) {
            winInfo += "player " + player.name() + " : " + player.victoryPoints() + "\n";
        }
        winInfo += "Wanna return to Homepage?";
        message.setLogInfo(winInfo);
        sendMessageToAll(message, true);
    }

}
