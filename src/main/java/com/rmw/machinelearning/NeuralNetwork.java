package com.rmw.machinelearning;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import processing.core.PVector;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.rmw.machinelearning.Configuration.AMOUNT_OF_INPUT_NEURONS;
import static com.rmw.machinelearning.Configuration.AMOUNT_OF_OUTPUT_NEURONS;
import static com.rmw.machinelearning.Configuration.HIDDEN_LAYERS_CONFIGURATION;
import static com.rmw.machinelearning.NeuronType.BIAS;
import static com.rmw.machinelearning.NeuronType.HIDDEN;
import static com.rmw.machinelearning.NeuronType.INPUT;
import static com.rmw.machinelearning.NeuronType.OUTPUT;
import static java.text.MessageFormat.format;

class NeuralNetwork {

    private static final String HIDDEN_NEURON_NAME_TEMPLATE = "HIDDEN{0}_{1}";
    private final Graph<Neuron, DefaultWeightedEdge> network = new SimpleDirectedWeightedGraph<>(DefaultWeightedEdge.class);
    private final Neuron biasNeuron = new Neuron(BIAS.name(), BIAS);
    private final List<Float> weights;

    NeuralNetwork(final List<Float> weights) {
        this.weights = weights;
        biasNeuron.setValue(1f);
        setupNeurons();
        setupConnections();
    }

    PVector react() {
        final List<Neuron> hiddenNeurons = getNeuronsOfType(HIDDEN);
        final List<Neuron> outputNeuron = getNeuronsOfType(OUTPUT);
        calculateValues(hiddenNeurons);
        calculateValues(outputNeuron);
        return new PVector(outputNeuron.get(0).getValue(), outputNeuron.get(1).getValue());
    }

    private void calculateValues(final List<Neuron> neurons) {
        neurons.forEach(neuron -> {
            final Set<DefaultWeightedEdge> connectedNeurons = network.incomingEdgesOf(neuron);
            connectedNeurons.forEach(edge -> {
                final Neuron sourceNeurone = network.getEdgeSource(edge);
                final float weight = (float) network.getEdgeWeight(edge);
                neuron.setValue(neuron.getValue() + (sourceNeurone.getValue() * weight));
            });
            final double sum = (double) neuron.getValue();
            final double resultOfActivationFunction;
            if (sum > 0) {
                resultOfActivationFunction = sum >= 1 ? 1 : 0;
            } else if (sum < 0){
                resultOfActivationFunction = sum <= 1 ? -1 : 0;
            } else {
                resultOfActivationFunction = 0;
            }
            neuron.setValue((float) resultOfActivationFunction);
        });
    }

    void setInputs(final List<Float> inputs) {
        final List<Neuron> inputNeurons = getNeuronsOfType(INPUT);
        for (int i = 0; i < 4; i++) {
            inputNeurons.get(i).setValue(inputs.get(i));
        }
        biasNeuron.setValue(1); //BIAS is always 1
    }

    private void setupNeurons() {
        generateLayer(INPUT, 0, AMOUNT_OF_INPUT_NEURONS);
        generateLayer(OUTPUT, 0, AMOUNT_OF_OUTPUT_NEURONS);
        for (int i = 0; i < HIDDEN_LAYERS_CONFIGURATION.size(); i++) {
            generateLayer(HIDDEN, i, HIDDEN_LAYERS_CONFIGURATION.get(i));
        }
        network.addVertex(biasNeuron);
    }

    private void generateLayer(final NeuronType type, final int layer, final int size) {
        for (int i = 0; i < size; i++) {
            network.addVertex(type.equals(HIDDEN)
                    ? createHiddenNeuron(layer, i)
                    : new Neuron(type.name() + "_" + i, type));
        }
    }

    private Neuron createHiddenNeuron(final int layer, final int index) {
        final Neuron neuron = new Neuron(format(HIDDEN_NEURON_NAME_TEMPLATE, layer, index), HIDDEN);
        neuron.setLayer(layer);
        return neuron;
    }

    private void setupConnections() {
        // TODO: support multiple layers of hidden layer
        final List<Neuron> inputNeurons = getNeuronsOfType(INPUT);
        final List<Neuron> hiddenNeurons = getNeuronsOfType(HIDDEN);
        final List<Neuron> outputNeurons = getNeuronsOfType(OUTPUT);
        int weightIndex = 0;
        weightIndex = createConnectionsBetweenGroups(inputNeurons, hiddenNeurons, weightIndex);
        weightIndex = createConnectionsBetweenGroups(hiddenNeurons, outputNeurons, weightIndex);
        createConnectionsForBiasNeuron(weightIndex);
    }

    private int createConnectionsBetweenGroups(final List<Neuron> fromGroup,
                                               final List<Neuron> toGroup,
                                               int weightIndex) {
        for (final Neuron fromNeuron : fromGroup) {
            for (final Neuron toNeuron : toGroup) {
                final float weight = weights.get(weightIndex);
                final DefaultWeightedEdge edge = network.addEdge(fromNeuron, toNeuron);
                network.setEdgeWeight(edge, weight);
                weightIndex++;
            }
        }
        return weightIndex;
    }

    private void createConnectionsForBiasNeuron(int weightIndex) {
        // find all neurons that are not input and excluding the bias neuron itself
        final List<Neuron> neuronList = network.vertexSet().stream()
                .filter(neuron -> !neuron.getType().equals(INPUT) && !neuron.getType().equals(BIAS))
                .collect(Collectors.toList());
        for (final Neuron toNeuron : neuronList) {
            final float weight = weights.get(weightIndex);
            final DefaultWeightedEdge edge = network.addEdge(biasNeuron, toNeuron);
            network.setEdgeWeight(edge, weight);
            weightIndex++;
        }
    }

    private List<Neuron> getNeuronsOfType(final NeuronType neuronType) {
        return network.vertexSet().stream()
                .filter(neuron -> neuron.getType().equals(neuronType)).collect(Collectors.toList());
    }
}
