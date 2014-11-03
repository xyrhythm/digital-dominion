package com.dominion.common;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class EventMessage {

    @JsonProperty("type")
    private String type;

    @JsonProperty("gameStatus")
    private String gameStatus;

    @JsonProperty("phaseName")
    private String phaseName;

    @JsonProperty("cardName")
    private String cardName;

    @JsonProperty("logInfo")
    private String logInfo = "";

    @JsonProperty("playerAction")
    private String playerAction = "";

    @JsonProperty("command")
    private String command = "";

    @JsonProperty("playResult")
    private String playResult = "";

    public EventMessage(String gameStatus, String phaseName, String cardName) {
        this.gameStatus = gameStatus;
        this.phaseName = phaseName;
        this.cardName = cardName;
    }

    public EventMessage() {
    }

    @JsonProperty("type")
    public String getType() {
        return type;
    }

    @JsonProperty("type")
    public void setType(final String type) {
        this.type = type;
    }

    @JsonProperty("gameStatus")
    public String getGameStatus() {
        return gameStatus;
    }

    @JsonProperty("gameStatus")
    public void setGameStatus(final String gameStatus) {
        this.gameStatus = gameStatus;
    }

    @JsonProperty("phaseName")
    public String getPhaseName() {
        return phaseName;
    }

    @JsonProperty("phaseName")
    public void setPhase(final String phaseName) {
        this.phaseName = phaseName;
    }

    @JsonProperty("cardName")
    public String getCardName() {
        return cardName;
    }

    @JsonProperty("cardName")
    public void setCardName(final String cardName) {
        this.cardName = cardName;
    }

    @JsonProperty("logInfo")
    public String getLogInfo() {
        return logInfo;
    }

    @JsonProperty("logInfo")
    public void setLogInfo(final String logInfo) {
        this.logInfo = logInfo;
    }

    @JsonProperty("playerAction")
    public String getPlayerAction() {
        return playerAction;
    }

    @JsonProperty("playerAction")
    public void setPlayerAction(final String playerAction) {
        this.playerAction = playerAction;
    }

    @JsonProperty("command")
    public String getCommand() {
        return command;
    }

    @JsonProperty("command")
    public void setCommand(final String command) {
        this.command = command;
    }

    @JsonProperty("playResult")
    public String getPlayResult() {
        return playResult;
    }

    @JsonProperty("playResult")
    public void setPlayResult(final String playResult) {
        this.playResult = playResult;
    }

    @JsonIgnore
    private Player receiver = null;

    public Player getReceiver() {
        return receiver;
    }

    public void setReceiver(final Player player) {
        this.receiver = player;
    }

}