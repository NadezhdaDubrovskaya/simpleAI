package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

class Player {

    private PApplet pApplet;
    private NeuralNetwork neuralNetwork;

    private List<Wall> walls;

    private PVector acceleration;
    private PVector position;
    private PVector velocity;
    private int radius = Configuration.PLAYER_RADIUS;

    private boolean dead = false;
    private float initialDistanceToTheGoal = Configuration.PLAYER_START_POSITION.dist(Configuration.GOAL_POSITION);
    private int survivedForXMoves = 0;
    private float fitnessScore = 0;


    Player(PApplet p, List<Float> weights) {
        pApplet = p;
        walls = Obstacles.getInstance(pApplet).getObstacles();
        position = new PVector(Configuration.PLAYER_START_POSITION.x, Configuration.PLAYER_START_POSITION.y);
        acceleration = new PVector(0, 0);
        velocity = new PVector(0, 0);
        neuralNetwork = new NeuralNetwork(weights);
    }

    void calculateFitness() {
        float distanceToTheGoal = Math.abs(position.x - Configuration.GOAL_POSITION.x);
        float distanceWalked = initialDistanceToTheGoal - distanceToTheGoal;
        if (!dead) {
            fitnessScore += 100;
        }
        fitnessScore += 100/distanceToTheGoal;
        fitnessScore += distanceWalked/survivedForXMoves;
    }

    float getFitnessScore() {
        return fitnessScore;
    }

    NeuralNetwork exposeNeuronNetwork() {
        return neuralNetwork;
    }

    void look() {

        neuralNetwork.clearInputs();

        checkThreshold(InputNeuron.RIGHT, pApplet.width - (position.x + radius));
        checkThreshold(InputNeuron.LEFT, position.x - radius);
        checkThreshold(InputNeuron.TOP, position.y - radius);
        checkThreshold(InputNeuron.BOTTOM, pApplet.height - (position.y + radius));

        for (Wall wall : walls) {
            if (position.y + radius >= wall.position.y && position.y - radius <= wall.position.y + wall.height) {
                checkThreshold(InputNeuron.RIGHT, wall.position.x - (position.x + radius));
                checkThreshold(InputNeuron.LEFT, (position.x - radius) - (wall.position.x + wall.width));
            }
            if (position.x + radius >= wall.position.x && position.x - radius <= wall.position.x + wall.width) {
                checkThreshold(InputNeuron.TOP, (position.y - radius) - (wall.position.y + wall.width));
                checkThreshold(InputNeuron.BOTTOM, wall.position.y - (position.y + radius));
            }
        }

        neuralNetwork.setInputNeuron(InputNeuron.BIAS.getCode(), 1); //bias neuron always returns 1

    }

    void think() {
        acceleration = neuralNetwork.react();
    }

    void move() {
        if (!dead) {
            velocity.add(acceleration);
            velocity.limit(Configuration.PLAYER_SPEED_LIMIT);
            boolean collided = isCollidingWithAnObstacle(position.x + velocity.x, position.y + velocity.y);
            if (collided) {
                velocity.rotate(180);
                // dead = true;
            } else {
                survivedForXMoves++;
            }
            position.add(velocity);
        }
    }

    void show() {
        pApplet.fill(Configuration.PLAYER_COLOR);
        pApplet.ellipse(position.x, position.y, radius * 2, radius * 2);
    }

    boolean isDead() {
        return dead;
    }

    private boolean isCollidingWithAnObstacle(float x, float y) {

        boolean collisionWithLeftBorder = x - radius <= 0;
        boolean collisionWithRightBorder = x + radius >= pApplet.width;
        boolean collisionWithTopBorder = y - radius <= 0;
        boolean collisionWithBottomBorder = y + radius >= pApplet.height;
        if (collisionWithLeftBorder || collisionWithRightBorder || collisionWithTopBorder || collisionWithBottomBorder) {
            return true;
        }

        for (Wall wall : walls) {
            boolean rightSideCollision = x + radius >= wall.position.x;
            boolean leftSideCollision = x - radius <= wall.position.x + wall.width;
            boolean bottomSideCollision = y + radius >= wall.position.y;
            boolean topSideCollision = y - radius <= wall.position.y + wall.height;
            if (rightSideCollision &&
                    leftSideCollision &&
                    bottomSideCollision &&
                    topSideCollision
            ) {
                return true;
            }
        }
        return false;
    }

    private void checkThreshold(InputNeuron inputNeuron, float distance) {
        if (inputNeuron.equals(InputNeuron.RIGHT) || inputNeuron.equals(InputNeuron.LEFT)) {
            if (distance > 0 && distance < Configuration.DISTANCE_THRESHOLD) {
                // PApplet.println("Obstacle is too close to the " + inputNeuron + " The distance is " + distance);
                neuralNetwork.setInputNeuron(inputNeuron.getCode(), 1 / distance);
            }
        } else {
            if (distance > 0 && distance < Configuration.DISTANCE_THRESHOLD) {
                // PApplet.println("Obstacle is too close to the " + inputNeuron + " The distance is " + distance);
                neuralNetwork.setInputNeuron(inputNeuron.getCode(), 1 / distance);
            }
        }
    }

}
