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


    private boolean dead = false;
    private int lifespan = 0;
    private int radius = 10;


    Player(PApplet p, List<Float> weights) {
        pApplet = p;
        position = new PVector(pApplet.width - 50, pApplet.height - 50);
        acceleration = new PVector(0, 0);
        velocity = new PVector(0, -5);
        neuronNetwork = new NeuronNetwork(weights);
    }

    boolean isDead() {
        return dead;
    }

    int getLifespan() {
        return lifespan;
    }

    NeuronNetwork exposeNeuronNetwork() {
        return neuronNetwork;
    }

    void look() {
        if (!dead) {

            float distanceRight = pApplet.width - (position.x + radius);
            float distanceLeft = position.x - radius;
            float distanceTop = position.y - radius;
            //float distanceBottom = pApplet.height - (position.y + radius);

            for (Wall wall : walls) {
                distanceRight = Math.min(distanceRight, wall.position.x - (position.x + radius));
                distanceLeft = Math.min(distanceLeft, (position.x - radius) - (wall.position.x + wall.width));
                distanceTop = Math.min(distanceTop, (position.y - radius) - (wall.position.y + wall.width));
                //distanceBottom = Math.min(distanceBottom, wall.position.y - (position.y + radius));
            }

            neuronNetwork.setInputNeuron(Direction.RIGHT.getInputNeuron(), distanceRight / 100);
            neuronNetwork.setInputNeuron(Direction.LEFT.getInputNeuron(), distanceLeft / 100);
            neuronNetwork.setInputNeuron(Direction.TOP.getInputNeuron(), distanceTop / 100);
            //neuronNetwork.setInputNeuron(Direction.BOTTOM.getInputNeuron(), distanceBottom / 100);

            /*
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
            */
        }
    }

    void think() {
        acceleration = neuronNetwork.react();
    }

    void move() {
        if (!dead) {
            velocity.add(acceleration);
            velocity.limit(5);
            position.add(velocity);
            lifespan++;
        }
    }

    void show() {
        int color = 255;
        pApplet.fill(color);
        pApplet.ellipse(position.x, position.y, radius * 2, radius * 2);
    }

    void checkForCollisions() {
        if (!dead) {
            boolean collisionWithLeftBorder = position.x - radius <= 0;
            boolean collisionWithRightBorder = position.x + radius >= pApplet.width;
            boolean collisionWithTopBorder = position.y - radius <= 0;
            boolean collisionWithBottomBorder = position.y + radius >= pApplet.height;
            if (collisionWithLeftBorder || collisionWithRightBorder || collisionWithTopBorder || collisionWithBottomBorder) {
                dead = true;
                return;
            }

            for (Wall wall : walls) {
                boolean rightSideCollision = position.x + radius >= wall.position.x;
                boolean leftSideCollision = position.x - radius <= wall.position.x + wall.width;
                boolean bottomSideCollision = position.y + radius >= wall.position.y;
                boolean topSideCollision = position.y - radius <= wall.position.y + wall.height;
                if (rightSideCollision &&
                        leftSideCollision &&
                        bottomSideCollision &&
                        topSideCollision
                ) {
                    dead = true;
                    return;
                }
            }
        }
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
