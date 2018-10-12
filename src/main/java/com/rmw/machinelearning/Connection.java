package com.rmw.machinelearning;

class Connection {

    private String name;
    private String from;
    private String to;
    private float weight;

    Connection(String from, String to, float weight) {
        this.name = from + to;
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    String getName() {
        return name;
    }

    String getFrom() {
        return from;
    }

    String getTo() {
        return to;
    }

    float getWeight() {
        return weight;
    }
}
