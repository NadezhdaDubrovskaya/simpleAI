package com.rmw.machinelearning;

import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

class NeuralNetwork {

    private List<Neuron> neurons = new ArrayList<>();
    private List<Connection> connections = new ArrayList<>();
    private List<Float> weights;

    private List<Neuron> inputNeurons = new ArrayList<>();
    private List<Neuron> hiddenNeurons = new ArrayList<>();
    private List<Neuron> outputNeurons = new ArrayList<>();

    NeuralNetwork(List<Float> weights) {
        this.weights = weights;
        setupNeurons();
        setupConnections();
    }

    PVector react() {
        //clearing the neuron network
        hiddenNeurons.forEach(neuron -> neuron.setValue(0));
        outputNeurons.forEach(neuron -> neuron.setValue(0));
        connections.forEach(connection -> {
            String destinationNeuron = connection.getTo();
            String linkedNeuron = connection.getFrom();
            float weight = connection.getWeight();
            float linkedNeuronValue = getNeuronValue(linkedNeuron);
            float destNeuronValue = getNeuronValue(destinationNeuron);
            float newValue = (float) Math.tanh(destNeuronValue + (weight * linkedNeuronValue));
            findNeuronByName(destinationNeuron).setValue(newValue);
        });
        float x = getNeuronValue("O1");
        float y = getNeuronValue("O2");
        inputNeurons.forEach(neuron -> neuron.setValue(0));
        return new PVector(x, y);
    }

    List<Float> getWeights() {
        return weights;
    }

    void setInputNeuron(String name, float value) {
        Neuron neuron = findNeuronByName(name);
        if (neuron != null) {
            neuron.setValue(value);
        } else {
            throw new Error("Neuron with " + name + " doesn't exist");
        }
    }

    void clearInputs() {
        inputNeurons.forEach(neuron -> neuron.setValue(0));
    }

    private float getNeuronValue(String name) {
        Neuron neuron = findNeuronByName(name);
        if (neuron != null) {
            return neuron.getValue();
        } else {
            throw new Error("Couldn't find neuron " + name);
        }
    }

    private Neuron findNeuronByName(String name) {
        return neurons.stream().filter(x -> x.name.equals(name)).findAny().orElse(null);
    }

    private void setupNeurons() {
        for (int i = 1; i <= Configuration.AMOUNT_OF_INPUT_NEURONS; i++) {
            Neuron neuron = new Neuron("I" + i, NeuronType.Input);
            neurons.add(neuron);
            inputNeurons.add(neuron);
        }
        for (int i = 1; i <= Configuration.AMOUNT_OF_HIDDEN_NEURONS; i++) {
            Neuron neuron = new Neuron("H" + i, NeuronType.Hidden);
            neurons.add(neuron);
            hiddenNeurons.add(neuron);
        }
        for (int i = 1; i <= Configuration.AMOUNT_OF_OUTPUT_NEURONS; i++) {
            Neuron neuron = new Neuron("O" + i, NeuronType.Output);
            neurons.add(neuron);
            outputNeurons.add(neuron);
        }
    }

    private void setupConnections() {
        connections = new ArrayList<>();
        int weightIndex = 0;
        for (Neuron neuron : neurons) {
            if (neuron.type == NeuronType.Input) {
                for (Neuron hiddenNeuron : hiddenNeurons) {
                    connections.add(new Connection(neuron.name, hiddenNeuron.name, weights.get(weightIndex++)));
                }
            }
            if (neuron.type == NeuronType.Hidden) {
                for (Neuron outputNeuron : outputNeurons) {
                    connections.add(new Connection(neuron.name, outputNeuron.name, weights.get(weightIndex++)));
                }
            }
        }
    }
}
