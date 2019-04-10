package com.rmw.machinelearning;

class Neuron {

    private String name;
    private NeuronType type;
    private float value;

    Neuron(final NeuronType type) {
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
}
