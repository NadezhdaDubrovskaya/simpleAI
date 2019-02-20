package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

import static com.rmw.machinelearning.Configuration.DISTANCE_THRESHOLD;
import static com.rmw.machinelearning.Configuration.PLAYER_COLOR;
import static com.rmw.machinelearning.Configuration.PLAYER_RADIUS;
import static com.rmw.machinelearning.Configuration.PLAYER_SPEED_LIMIT;
import static com.rmw.machinelearning.Configuration.PLAYER_START_POSITION;
import static com.rmw.machinelearning.Direction.BOTTOM;
import static com.rmw.machinelearning.Direction.LEFT;
import static com.rmw.machinelearning.Direction.RIGHT;
import static com.rmw.machinelearning.Direction.TOP;
import static com.rmw.machinelearning.DistanceUtil.calculateDistance;

class Player extends ScreenObject {

    private final PApplet pApplet;
    private final NeuralNetwork neuralNetwork;
    private final PVector velocity;
    private PVector acceleration;
    private boolean dead;
    private int survivedForXMoves;
    private float fitnessScore;

    Player(final PApplet p, final List<Float> weights) {
        pApplet = p;
        position = position.set(PLAYER_START_POSITION.x, PLAYER_START_POSITION.y);
        acceleration = new PVector(0, 0);
        velocity = new PVector(0, 0);
        neuralNetwork = new NeuralNetwork(weights);
    }

    @Override
    void update() {
        look();
        think();
        move();
        show();
    }

    @Override
    float getTopBorder() {
        return position.y - PLAYER_RADIUS;
    }

    @Override
    float getBottomBorder() {
        return position.y + PLAYER_RADIUS;
    }

    @Override
    float getLeftBorder() {
        return position.x - PLAYER_RADIUS;
    }

    @Override
    float getRightBorder() {
        return position.x + PLAYER_RADIUS;
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

    NeuralNetwork exposeNeuronNetwork() {
        return neuralNetwork;
    }

    private void look() {

        neuralNetwork.clearInputs();

        checkThreshold(RIGHT, pApplet.width - (position.x + PLAYER_RADIUS));
        checkThreshold(LEFT, position.x - PLAYER_RADIUS);
        checkThreshold(TOP, position.y - PLAYER_RADIUS);
        checkThreshold(BOTTOM, pApplet.height - (position.y + PLAYER_RADIUS));

        //TODO we still go through the remaining obstacles even after we found the one that "kills" us
        Obstacles.getInstance(pApplet).getObstacles().forEach(obstacle -> {
            final Distance distance = calculateDistance(this, obstacle);
            if (distance != null) {
                if (distance.getDistance() == 0) {
                    dead = true;
                }
                switch (distance.getDirection()) {
                    case TOP:
                        checkThreshold(TOP, distance.getDistance());
                    case BOTTOM:
                        checkThreshold(BOTTOM, distance.getDistance());
                    case LEFT:
                        checkThreshold(LEFT, distance.getDistance());
                    case RIGHT:
                        checkThreshold(RIGHT, distance.getDistance());
                    default:
                        break;
                }
            }
        });

        neuralNetwork.setInputNeuron(Direction.BIAS.getCode(), 1); //bias neuron always returns 1

    }

    private void think() {
        acceleration = neuralNetwork.react();
    }

    private void move() {
        if (!dead) {
            velocity.add(acceleration);
            velocity.limit(PLAYER_SPEED_LIMIT);
            survivedForXMoves++;
            position.add(velocity);
        }
    }

    private void show() {
        pApplet.fill(PLAYER_COLOR.v1, PLAYER_COLOR.v2, PLAYER_COLOR.v3);
        pApplet.ellipse(position.x, position.y, PLAYER_RADIUS * 2, PLAYER_RADIUS * 2);
    }

    boolean isDead() {
        return dead;
    }

    //TODO the values can be overriden with further objects, add check to update neuron only if the object is closer
    private void checkThreshold(final Direction direction, final float distance) {
        if (direction.equals(RIGHT) || direction.equals(LEFT)) {
            if (distance > 0 && distance < DISTANCE_THRESHOLD) {
                // PApplet.println("Obstacle is too close to the " + direction + " The distance is " + distance);
                neuralNetwork.setInputNeuron(direction.getCode(), 1 / distance);
            }
        } else {
            if (distance > 0 && distance < DISTANCE_THRESHOLD) {
                // PApplet.println("Obstacle is too close to the " + direction + " The distance is " + distance);
                neuralNetwork.setInputNeuron(direction.getCode(), 1 / distance);
            }
        }
    }

}
