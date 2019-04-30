package com.rmw.machinelearning;

import processing.core.PVector;

import java.util.List;

import static com.rmw.machinelearning.DistanceUtil.calculateDistance;

class Brain {

    private final List<Float> weights;
    private final NeuralNetwork neuralNetwork;
    private final Vision vision;

    Brain(final List<Float> weights) {
        this.weights = weights;
        neuralNetwork = new NeuralNetwork(weights);
        vision = new Vision();
    }

    List<Float> getWeights() {
        return weights;
    }

    void look() {
/*        brain.look();
        vision.reset();
        for (final ScreenObject obstacle : getObstacles()) {
            final Side side = calculateDistance(this, obstacle);
            if (side != null) {
                checkThreshold(side);
            }
        }
        neuralNetwork.setInputs(vision);*/
    }

    void react() {
/*        final PVector reaction = neuralNetwork.react();
        if (reaction.x != 0 || reaction.y != 0) {
            setVelocity(reaction.x, reaction.y);
        } else {
            stop();
        }*/
    }


    private void checkThreshold(final Side side) {
        final Direction direction = side.getDirection();
        final float dist = side.getDistance();
        final float newNeuronValue = 100 / dist;
        //update input value only in case the new object is closer
        if (newNeuronValue > vision.get(direction)) {
            vision.put(direction, newNeuronValue);
        }
    }

}
