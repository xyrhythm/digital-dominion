package com.dominion.common;

import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class PlayerStatus {

    @JsonProperty("curPlayer")
    private String curPlayer;

    @JsonProperty("curPhase")
    private String curPhase;

    @JsonProperty("numAction")
    private int numAction;

    @JsonProperty("numTreasure")
    private int numTreasure;

    @JsonProperty("numBuy")
    private int numBuy;

    @JsonProperty("curPlayer")
    public String getCurPlayer() {
        return curPlayer;
    }

    @JsonProperty("curPlayer")
    public void setCurPlayer(final String curPlayer) {
        this.curPlayer = curPlayer;
    }

    @JsonProperty("curPhase")
    public String getCurPhase() {
        return curPhase;
    }

    @JsonProperty("curPhase")
    public void setCurPhase(final String curPhase) {
        this.curPhase = curPhase;
    }

    @JsonProperty("numAction")
    public int getNumAction() {
        return numAction;
    }

    @JsonProperty("numAction")
    public void setNumAction(final int numActoin) {
        this.numAction = numActoin;
    }

    @JsonProperty("numTreasure")
    public int getNumTreasure() {
        return numTreasure;
    }

    @JsonProperty("numTreasure")
    public void setNumTreasure(final int numTreasure) {
        this.numTreasure = numTreasure;
    }

    @JsonProperty("numBuy")
    public int getNumBuy() {
        return numBuy;
    }

    @JsonProperty("numBuy")
    public void setNumBuy(final int numBuy) {
        this.numBuy = numBuy;
    }
}
