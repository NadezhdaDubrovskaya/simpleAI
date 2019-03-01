package com.rmw.machinelearning;

import com.rmw.machinelearning.model.Connection;
import com.rmw.machinelearning.model.Neuron;
import processing.core.PVector;

import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import static com.rmw.machinelearning.Configuration.AMOUNT_OF_INPUT_NEURONS;
import static com.rmw.machinelearning.Configuration.AMOUNT_OF_OUTPUT_NEURONS;
import static com.rmw.machinelearning.Configuration.HIDDEN_LAYERS_CONFIGURATION;
import static com.rmw.machinelearning.NeuronType.Bias;
import static com.rmw.machinelearning.NeuronType.Hidden;
import static com.rmw.machinelearning.NeuronType.Input;
import static com.rmw.machinelearning.NeuronType.Output;

class NeuralNetwork {

    private final Neuron biasNeuron = new Neuron(Bias);
    private final List<List<Neuron>> neurons = new LinkedList<>();
    private final List<Connection> connections = new LinkedList<>();
    private List<Float> weights;

    NeuralNetwork(final List<Float> weights) {
        biasNeuron.setValue(1);
        this.weights = weights;
        setupNeurons();
        setupConnections();
    }

    PVector react() {
        //skip input layer
        for (int i = 1; i < neurons.size(); i++) {
            neurons.get(i).forEach(neuron -> neuron.setValue(0));
        }
        final List<Neuron> outputLayer = neurons.get(neurons.size() - 1);
        outputLayer.forEach(this::calculateNeuronValue);
        return new PVector(outputLayer.get(0).getValue(), outputLayer.get(1).getValue());
    }

    private void calculateNeuronValue(final Neuron neuron) {
        float value = 0;
        final List<Connection> linkedConnections =
                connections.stream()
                        .filter(connection -> connection.getTo().equals(neuron))
                        .collect(Collectors.toList());
        for (final Connection connection : linkedConnections) {
            final Neuron fromNeuron = connection.getFrom();
            if (fromNeuron.getType() != Input && fromNeuron.getType() != Bias) {
                calculateNeuronValue(fromNeuron);
            }
            value += fromNeuron.getValue() * connection.getWeight();
        }
        neuron.setValue(value);
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

    void setInputs(final Vision vision) {
        final List<Neuron> inputNeurons = neurons.get(0);
        vision.getSides().forEach((direction, value) ->
                inputNeurons.get(direction.getIndex()).setValue(value));
    }

    private void setupNeurons() {
        generateGroupOfNeurons(Input, AMOUNT_OF_INPUT_NEURONS);
        HIDDEN_LAYERS_CONFIGURATION.forEach(amount -> generateGroupOfNeurons(Hidden, amount));
        generateGroupOfNeurons(Output, AMOUNT_OF_OUTPUT_NEURONS);
    }

    private void generateGroupOfNeurons(final NeuronType type, final int size) {
        final List<Neuron> group = new LinkedList<>();
        for (int i = 0; i < size; i++) {
            group.add(new Neuron(type));
        }
        neurons.add(group);
    }

    private void setupConnections() {
        int weightIndex = 0;
        for (int i = 0; i < neurons.size() - 1; i++) {
            final List<Neuron> currentGroup = neurons.get(i);
            final List<Neuron> nextGroup = neurons.get(i + 1);
            weightIndex = createConnectionsBetweenGroups(currentGroup, nextGroup, weightIndex);
        }
    }

    private int createConnectionsBetweenGroups(final List<Neuron> fromGroup,
                                               final List<Neuron> toGroup,
                                               int weightIndex) {
        for (final Neuron fromNeuron : fromGroup) {
            for (final Neuron toNeuron : toGroup) {
                connections.add(new Connection(fromNeuron, toNeuron, weights.get(weightIndex++)));
            }
        }
        // create connections for bias neuron
        for (final Neuron toNeuron : toGroup) {
            connections.add(new Connection(biasNeuron, toNeuron, weights.get(weightIndex++)));
        }
        return weightIndex;
    }

}
