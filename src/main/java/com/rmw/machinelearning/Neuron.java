package com.rmw.machinelearning;

public class Neuron {

    String name;
    float value;
    NeuronType type;

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
