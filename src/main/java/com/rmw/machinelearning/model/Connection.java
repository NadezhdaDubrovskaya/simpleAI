package com.rmw.machinelearning.model;

public class Connection {

    private final String name;
    private final String from;
    private final String to;
    private final float weight;

    public Connection(final String from, final String to, final float weight) {
        name = from + to;
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public float getWeight() {
        return weight;
    }
}
