package com.dominion.common;

import java.util.List;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.annotate.JsonSerialize;

@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class GameInfo {

    @JsonProperty("id")
    private int gameId;

    @JsonProperty("deck")
    private String deckName;

    @JsonProperty("creator")
    private String creatorName;

    @JsonProperty("players")
    private List<String> playersName;

    @JsonProperty("active")
    private boolean isActive;

    @JsonProperty("id")
    public int getId() {
        return gameId;
    }

    @JsonProperty("id")
    public void setId(final int id) {
        gameId = id;
    }

    @JsonProperty("deck")
    public String getDeck() {
        return deckName;
    }

    @JsonProperty("deck")
    public void setDeck(final String deck) {
        deckName = deck;
    }

    @JsonProperty("creator")
    public String getCreator() {
        return creatorName;
    }

    @JsonProperty("creator")
    public void setCreator(final String creator) {
        creatorName = creator;
    }

    @JsonProperty("players")
    public List<String> getPlayers() {
        return playersName;
    }

    @JsonProperty("players")
    public void setPlayers(List<String> players) {
        playersName = players;
    }

    @JsonProperty("active")
    public boolean isActive() {
        return isActive;
    }

    @JsonProperty("active")
    public void setActive(final boolean active) {
        isActive = active;
    }
}
