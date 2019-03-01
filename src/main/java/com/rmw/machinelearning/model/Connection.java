package com.rmw.machinelearning.model;

public class Connection {

    private final Neuron from;
    private final Neuron to;
    private float weight;

    public Connection(final Neuron from, final Neuron to, final float weight) {
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    public Neuron getFrom() {
        return from;
    }

    public Neuron getTo() {
        return to;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(final float weight) {
        this.weight = weight;
    }
}
