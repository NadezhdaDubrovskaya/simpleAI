package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

import static com.rmw.machinelearning.Configuration.Colour;
import static com.rmw.machinelearning.Configuration.DISTANCE_THRESHOLD;
import static com.rmw.machinelearning.Configuration.PLAYER_COLOR;
import static com.rmw.machinelearning.Configuration.PLAYER_SPEED_LIMIT;
import static com.rmw.machinelearning.Configuration.PLAYER_START_POSITION;
import static com.rmw.machinelearning.DistanceUtil.calculateDistance;

class Player extends CircularObject {

    private final NeuralNetwork neuralNetwork;
    private final Vision vision = new Vision();
    private final PVector velocity = new PVector();
    private final PVector acceleration = new PVector();
    private boolean dead;
    private int survivedForXMoves;
    private int fitnessScore;

    Player(final PApplet p, final List<Float> weights) {
        super(p);
        neuralNetwork = new NeuralNetwork(weights);
        reset();
    }

    @Override
    void reset() {
        getPosition().set(PLAYER_START_POSITION.x, PLAYER_START_POSITION.y);
        velocity.set(0, 0);
        acceleration.set(0, 0);
        dead = false;
        survivedForXMoves = 0;
        fitnessScore = 0;
        colour = PLAYER_COLOR;
    }

    @Override
    void update() {
        if (!dead) {
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
        for (final ScreenObject obstacle : Obstacles.getInstance(pApplet).getObstacles()) {
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
            acceleration.set(reaction.x, reaction.y);
        } else {
            //if there are no signals from the brain - stop moving
            acceleration.set(0, 0);
            velocity.set(0, 0);
        }
    }

    private void move() {
        if (!dead) {
            velocity.add(acceleration);
            velocity.limit(PLAYER_SPEED_LIMIT);
            survivedForXMoves++;
            getPosition().add(velocity);
        }
    }

    boolean isDead() {
        return dead;
    }

    private void checkThreshold(final Side side) {
        final Direction direction = side.getDirection();
        final float dist = side.getDistance();
        if (dist <= 0) {
            dead = true;
            colour = new Colour(150, 150, 150);
            return;
        }
        if (dist < DISTANCE_THRESHOLD) {
            final float newNeuronValue = 1 / dist;
            //update input value only in case the new object is closer
            if (newNeuronValue > vision.get(direction)) {
                vision.put(direction, newNeuronValue);
            }
        }
    }

}
