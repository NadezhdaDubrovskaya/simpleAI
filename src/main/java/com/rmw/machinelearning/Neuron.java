package com.rmw.machinelearning;

class Neuron {

    private String name;
    private NeuronType type;
    private float value;
    private int layer; // used for hidden neurons

    Neuron(final NeuronType type) {
        this.type = type;
    }

    Neuron(final String name, final NeuronType type) {
        this.name = name;
        this.type = type;
    }

    float getValue() {
        return value;
    }

    void setValue(final float value) {
        this.value = value;
    }

    String getName() {
        return name;
    }

    NeuronType getType() {
        return type;
    }

    int getLayer() {
        return layer;
    }

    void setLayer(final int layer) {
        this.layer = layer;
    }

    @Override
    public String toString() {
        return name;
    }
}
