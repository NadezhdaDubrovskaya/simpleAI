package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

import static com.rmw.machinelearning.Configuration.Colour;
import static com.rmw.machinelearning.Configuration.DISTANCE_THRESHOLD;
import static com.rmw.machinelearning.Configuration.PLAYER_COLOR;
import static com.rmw.machinelearning.Configuration.PLAYER_SPEED_LIMIT;
import static com.rmw.machinelearning.Configuration.PLAYER_START_POSITION;
import static com.rmw.machinelearning.Direction.BOTTOM;
import static com.rmw.machinelearning.Direction.LEFT;
import static com.rmw.machinelearning.Direction.RIGHT;
import static com.rmw.machinelearning.Direction.TOP;
import static com.rmw.machinelearning.DistanceUtil.calculateDistance;

class Player extends CircularObject {

    private final NeuralNetwork neuralNetwork;
    private final PVector velocity;
    private PVector acceleration;
    private boolean dead;
    private int survivedForXMoves;
    private float fitnessScore;

    Player(final PApplet p, final List<Float> weights) {
        super(p);
        getPosition().set(PLAYER_START_POSITION.x, PLAYER_START_POSITION.y);
        acceleration = new PVector(0, 0);
        velocity = new PVector(0, 0);
        neuralNetwork = new NeuralNetwork(weights);
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

    float getFitnessScore() {
        return fitnessScore;
    }

    void calculateFitness() {
        if (!dead) {
            fitnessScore += 100;
        }
        fitnessScore += survivedForXMoves;
    }

    List<Float> getWeights() {
        return neuralNetwork.getWeights();
    }

    private void look() {

        neuralNetwork.clearInputs();

        // look for the borders
        checkThreshold(new Distance(RIGHT, pApplet.width - (getPosition().x + radius)));
        checkThreshold(new Distance(LEFT, getPosition().x - radius));
        checkThreshold(new Distance(TOP, getPosition().y - radius));
        checkThreshold(new Distance(BOTTOM, pApplet.height - (getPosition().y + radius)));

        for (final ScreenObject obstacle : Obstacles.getInstance(pApplet).getObstacles()) {
            final Distance distance = calculateDistance(this, obstacle);
            if (distance != null) {
                if (distance.getDistance() == 0) {
                    dead = true;
                    colour = new Colour(150, 150, 150);
                    break;
                }
                checkThreshold(distance);
            }
        }

        neuralNetwork.setInputNeuron(Direction.BIAS.getCode(), 1); //bias neuron always returns 1

    }

    private void think() {
        final PVector reaction = neuralNetwork.react();
        if (reaction.x != 0 || reaction.y != 0) {
            acceleration = reaction;
        } else {
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

    private void checkThreshold(final Distance distance) {
        final Direction direction = distance.getDirection();
        final float dist = distance.getDistance();
        if (dist < 0) {
            dead = true;
            colour = new Colour(150, 150, 150);
        }
        if (dist < DISTANCE_THRESHOLD) {
            final float newNeuronValue = 1 / dist;
            if (newNeuronValue > neuralNetwork.getNeuronValue(direction.getCode())) {
                neuralNetwork.setInputNeuron(direction.getCode(), 1 / dist);
            }
        }
    }

}
