package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

class Player {

    private PApplet pApplet;
    private NeuronNetwork neuronNetwork;

    private List<Wall> walls = Obstacles.getInstance().getObstacles();

    private PVector acceleration;
    private PVector position;
    private PVector velocity;
    private int radius = 10;

    private int collidedTimes = 0; //how much time the ball collided with the wall
    private int fitnessScore = 1000;


    Player(PApplet p, List<Float> weights) {
        pApplet = p;
        position = new PVector(400, 200);
        acceleration = new PVector(0, 0);
        velocity = new PVector(5, 0);
        neuronNetwork = new NeuronNetwork(weights);
    }

    void calculateFitness() {
        fitnessScore -= collidedTimes;
    }

    int getFitnessScore() {
        return fitnessScore;
    }

    NeuronNetwork exposeNeuronNetwork() {
        return neuronNetwork;
    }

    void look() {
        checkThreshold(Direction.RIGHT, pApplet.width - (position.x + radius));
        checkThreshold(Direction.LEFT, position.x - radius);
        checkThreshold(Direction.TOP, position.y - radius);
        checkThreshold(Direction.BOTTOM, pApplet.height - (position.y + radius));

        for (Wall wall : walls) {
            if (position.y + radius >= wall.position.y && position.y - radius <= wall.position.y + wall.height) {
                checkThreshold(Direction.RIGHT, wall.position.x - (position.x + radius));
                checkThreshold(Direction.LEFT, (position.x - radius) - (wall.position.x + wall.width));
            }
            if (position.x + radius >= wall.position.x && position.x - radius <= wall.position.x + wall.width) {
                checkThreshold(Direction.TOP, (position.y - radius) - (wall.position.y + wall.width));
                checkThreshold(Direction.BOTTOM, wall.position.y - (position.y + radius));
            }
        }
    }


    void think() {
        acceleration = neuronNetwork.react();
    }

    void move() {

        velocity.add(acceleration);
        velocity.limit(3);
        boolean collided = isCollidingWithAnObstacle(position.x + velocity.x, position.y + velocity.y);
        if (collided) {
            collidedTimes++;
            velocity.rotate(180);
        }
        position.add(velocity);

    }

    void show() {
        int color = 255;
        pApplet.fill(color);
        pApplet.ellipse(position.x, position.y, radius * 2, radius * 2);
    }

    private boolean isCollidingWithAnObstacle(float x, float y) {

        boolean collisionWithLeftBorder = x - radius <= 0;
        boolean collisionWithRightBorder = x + radius >= pApplet.width;
        boolean collisionWithTopBorder = y - radius <= 0;
        boolean collisionWithBottomBorder = y + radius >= pApplet.height;
        if (collisionWithLeftBorder || collisionWithRightBorder || collisionWithTopBorder || collisionWithBottomBorder) {
            //dead = true;
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
                //dead = true;
                return true;
            }
        }
        return false;
    }


    private void checkThreshold(Direction direction, float distance) {
        float distanceXThreshold = 10;
        float distanceYThreshold = 10;
        if (direction.equals(Direction.RIGHT) || direction.equals(Direction.LEFT)) {
            if (distance > 0 && distance < distanceXThreshold) {
                // PApplet.println("Obstacle is too close to the " + direction + " The distance is " + distance);
                neuronNetwork.setInputNeuron(direction.getInputNeuron(), distanceXThreshold / distance);
            }
        } else {
            if (distance > 0 && distance < distanceYThreshold) {
                // PApplet.println("Obstacle is too close to the " + direction + " The distance is " + distance);
                neuronNetwork.setInputNeuron(direction.getInputNeuron(), distanceYThreshold / distance);
            }
        }
    }
}
