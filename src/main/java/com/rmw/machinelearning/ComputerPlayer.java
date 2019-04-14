package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

import static com.rmw.machinelearning.DistanceUtil.calculateDistance;

class ComputerPlayer extends Player {

    private final NeuralNetwork neuralNetwork;
    private final Vision vision = new Vision();
    private int survivedForXMoves;
    private int fitnessScore;

    ComputerPlayer(final PApplet p, final List<Float> weights) {
        super(p);
        neuralNetwork = new NeuralNetwork(weights);
        reset();
    }

    @Override
    void reset() {
        super.reset();
        survivedForXMoves = 0;
        fitnessScore = 0;
    }

    @Override
    void update() {
        if (!isDead()) {
            look();
            think();
            move();
            show();
        }
    }

    int getFitnessScore() {
        return fitnessScore;
    }

    void calculateFitness() {
        fitnessScore = survivedForXMoves;
    }

    List<Float> getWeights() {
        return neuralNetwork.getWeights();
    }

    void changeWeights(final List<Float> weights) {
        neuralNetwork.setWeights(weights);
    }

    private void look() {
        vision.reset();
        for (final ScreenObject obstacle : Obstacles.getInstance(getPApplet()).getObstacles()) {
            final Side side = calculateDistance(this, obstacle);
            if (side != null) {
                checkThreshold(side);
            }
        }
        neuralNetwork.setInputs(vision);
    }

    private void think() {
        final PVector reaction = neuralNetwork.react();
        if (reaction.x != 0 || reaction.y != 0) {
            setVelocity(reaction.x, reaction.y);
        } else {
            stop();
        }
    }

    @Override
    void move() {
        super.move();
        if (!isDead()) {
            survivedForXMoves++;
        }
    }

    private void checkThreshold(final Side side) {
        final Direction direction = side.getDirection();
        final float dist = side.getDistance();
        if (dist <= 0) {
            setDead(true);
            setColour(150, 150, 150);
            return;
        }
        final float newNeuronValue = 100 / dist;
        //update input value only in case the new object is closer
        if (newNeuronValue > vision.get(direction)) {
            vision.put(direction, newNeuronValue);
        }
    }
}
