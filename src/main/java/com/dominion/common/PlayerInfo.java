package com.dominion.common;

public class PlayerInfo {

    private final String name;
    private final int order;

    public PlayerInfo(final String name, final int order) {
        this.name = name;
        this.order = order;
    }

    public String name() {
        return name;
    }

    public int order() {
        return order;
    }
}
