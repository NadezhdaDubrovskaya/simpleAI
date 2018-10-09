package com.rmw.machinelearning;

import processing.core.PVector;

import java.util.List;

class Brain {

    private NeuronNetwork neuronNetwork;

    Brain(List<Float> weights) {
        neuronNetwork = new NeuronNetwork();
        neuronNetwork.setWeights(weights);
    }

    PVector react() {
        neuronNetwork.calculate();
        float x = neuronNetwork.getNeuronValue("O1");
        float y = neuronNetwork.getNeuronValue("O2");
        return new PVector(x, y);

    }

    void setInputNeuron(String name, float value) {
        neuronNetwork.setInputNeuron(name, value);
    }

}
