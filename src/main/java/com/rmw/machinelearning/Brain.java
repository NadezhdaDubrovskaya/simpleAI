package com.rmw.machinelearning;

import processing.core.PVector;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

class Brain {

    private final List<Float> weights;
    private final NeuralNetwork neuralNetwork;
    private final GameScreenGraph gameScreenGraph;

    Brain(final List<Float> weights, final GameScreenGraph gameScreenGraph) {
        this.weights = weights;
        this.gameScreenGraph = gameScreenGraph;
        neuralNetwork = new NeuralNetwork(weights);
    }


    void look(final PVector currentPlayerPosition) {
        final List<Float> inputs = new ArrayList<>();
        for (final Direction direction : Direction.values()) {
            if (direction == Direction.BIAS) {
                break;
            }
            final boolean obstaclePresent = gameScreenGraph.isThereAnObstacleNearBy(currentPlayerPosition, direction);
            inputs.add(obstaclePresent ? 1f : 0f);
        }
        neuralNetwork.setInputs(inputs);
    }

    PVector react() {
        return neuralNetwork.react();
    }

    List<Float> getWeights() {
        return weights;
    }

    @Override
    public String toString() {
        return Arrays.toString(weights.toArray());
    }
}
