package com.rmw.machinelearning.model;

import com.rmw.machinelearning.NeuronType;

public class Neuron {

    private String name;
    private NeuronType type;
    private float value;

    public Neuron(final String neuronName, final NeuronType type) {
        name = neuronName;
        this.type = type;
    }

    public void setValue (final float value) {
        this.value = value;
    }

    public float getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public NeuronType getType() {
        return type;
    }
}
