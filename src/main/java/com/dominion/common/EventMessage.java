package com.dominion.common;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_EMPTY)
public class EventMessage {

    @JsonProperty("gameStatus")
    private String gameStatus;

    @JsonProperty("phaseName")
    private String phaseName;

    @JsonProperty("cardName")
    private String cardName;

    public EventMessage(String gameStatus, String phaseName, String cardName) {
        this.gameStatus = gameStatus;
        this.phaseName = phaseName;
        this.cardName = cardName;
    }

    public EventMessage() {
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
}