package com.rmw.machinelearning;

public class Connection {

    String name;
    String from;
    String to;
    float weight;

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
