package com.rmw.machinelearning;

class Neuron {

    String name;
    NeuronType type;
    private float value;

    Neuron(String neuronName, NeuronType type) {
        this.name = neuronName;
        this.type = type;
    }

    void setValue (float value) {
        this.value = value;
    }

    float getValue() {
        return value;
    }

}
