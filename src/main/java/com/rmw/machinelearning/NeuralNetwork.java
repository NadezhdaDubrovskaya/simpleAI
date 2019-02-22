package com.rmw.machinelearning;

import com.rmw.machinelearning.model.Connection;
import com.rmw.machinelearning.model.Neuron;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

class NeuralNetwork {

    private final List<Neuron> neurons = new ArrayList<>();
    private final List<Neuron> inputNeurons = new ArrayList<>();
    private final List<Neuron> hiddenNeurons = new ArrayList<>();
    private final List<Neuron> outputNeurons = new ArrayList<>();
    private final List<Connection> connections = new ArrayList<>();
    private List<Float> weights;

    NeuralNetwork(final List<Float> weights) {
        this.weights = new LinkedList<>(weights);
        setupNeurons();
        setupConnections();
    }

    PVector react() {
        //clearing the neuron network
        hiddenNeurons.forEach(neuron -> neuron.setValue(0));
        outputNeurons.forEach(neuron -> neuron.setValue(0));
        connections.forEach(connection -> {
            final String destinationNeuron = connection.getTo();
            final String linkedNeuron = connection.getFrom();
            final float weight = connection.getWeight();
            final float linkedNeuronValue = getNeuronValue(linkedNeuron);
            final float destNeuronValue = getNeuronValue(destinationNeuron);
            final float newValue = (float) Math.tanh(destNeuronValue + (weight * linkedNeuronValue));
            findNeuronByName(destinationNeuron).setValue(newValue);
        });
        final float x = getNeuronValue("O1");
        final float y = getNeuronValue("O2");
        clearInputs();
        return new PVector(x, y);
    }

    List<Float> getWeights() {
        return weights;
    }

    void setWeights(final List<Float> weights) {
        this.weights = weights;
        for (int i = 0; i < connections.size(); i++) {
            connections.get(i).setWeight(weights.get(i));
        }
    }

    void setInputNeuron(final String name, final float value) {
        final Neuron neuron = findNeuronByName(name);
        if (neuron != null) {
            neuron.setValue(value);
        } else {
            throw new Error("Neuron with " + name + " doesn't exist");
        }
    }

    void clearInputs() {
        inputNeurons.forEach(neuron -> neuron.setValue(0));
    }

    float getNeuronValue(final String name) {
        final Neuron neuron = findNeuronByName(name);
        if (neuron != null) {
            return neuron.getValue();
        } else {
            throw new Error("Couldn't find neuron " + name);
        }
    }

    private Neuron findNeuronByName(final String name) {
        return neurons.stream().filter(x -> x.getName().equals(name)).findAny().orElse(null);
    }

    private void setupNeurons() {
        for (int i = 1; i <= Configuration.AMOUNT_OF_INPUT_NEURONS; i++) {
            final Neuron neuron = new Neuron("I" + i, NeuronType.Input);
            neurons.add(neuron);
            inputNeurons.add(neuron);
        }
        for (int i = 1; i <= Configuration.AMOUNT_OF_HIDDEN_NEURONS; i++) {
            final Neuron neuron = new Neuron("H" + i, NeuronType.Hidden);
            neurons.add(neuron);
            hiddenNeurons.add(neuron);
        }
        for (int i = 1; i <= Configuration.AMOUNT_OF_OUTPUT_NEURONS; i++) {
            final Neuron neuron = new Neuron("O" + i, NeuronType.Output);
            neurons.add(neuron);
            outputNeurons.add(neuron);
        }
    }

    private void setupConnections() {
        int weightIndex = 0;
        for (final Neuron neuron : neurons) {
            if (neuron.getType() == NeuronType.Input) {
                for (final Neuron hiddenNeuron : hiddenNeurons) {
                    connections.add(new Connection(neuron.getName(), hiddenNeuron.getName(), weights.get(weightIndex++)));
                }
            }
            if (neuron.getType() == NeuronType.Hidden) {
                for (final Neuron outputNeuron : outputNeurons) {
                    connections.add(new Connection(neuron.getName(), outputNeuron.getName(), weights.get(weightIndex++)));
                }
            }
        }
    }
}
