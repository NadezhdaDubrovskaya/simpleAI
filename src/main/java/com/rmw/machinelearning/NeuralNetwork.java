package com.rmw.machinelearning;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.jgrapht.traverse.DepthFirstIterator;
import processing.core.PVector;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.io.StringWriter;
import java.io.Writer;
import java.rmi.server.ExportException;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.*;
import org.jgrapht.graph.*;

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
    private final Neuron biasNeuron;
    private List<Float> weights;

    NeuralNetwork(final List<Float> weights) {
        biasNeuron = new Neuron(BIAS.name(), BIAS);
        biasNeuron.setValue(1f);
        this.weights = weights;
        setupNeurons();
        setupConnections();

        Iterator<Neuron> iter = new DepthFirstIterator<>(network);
        while (iter.hasNext()) {
            Neuron vertex = iter.next();
            System.out.println(
                    "Vertex " + vertex + " is connected to: "
                            + network.edgesOf(vertex));
        }

    }

    PVector react() {
        throw new NotImplementedException();
    }

    List<Float> getWeights() {
        return weights;
    }

    void setWeights(final List<Float> weights) {
        throw new NotImplementedException();
    }

    void setInputs(final Vision vision) {
        final List<Neuron> inputNeurons = getNeuronsOfType(INPUT);
        vision.getSides().forEach((direction, value) ->
                inputNeurons.get(direction.getIndex()).setValue(value));
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
