package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

class Player {

    private PApplet parent;
    private List<Wall> walls = Obstacles.getInstance().getObstacles();
    private PVector acceleration;
    private PVector position;
    private PVector velocity;
    private Brain brain;
    private boolean dead = false;
    private int radius = 10;
    private int color = 255;

    Player(PApplet p, List<Float> weights) {
        parent = p;
        position = new PVector(400, 200);
        acceleration = new PVector(0, 0);
        velocity = new PVector(5, 0);
        brain = new Brain(weights);
    }

    boolean isDead() {
        return dead;
    }

    void setAcceleration(PVector acc) {
        acceleration = acc;
    }

    void look() {
        if (!dead) {

            checkThreshold(Direction.RIGHT, parent.width - (position.x + radius));
            checkThreshold(Direction.LEFT, position.x - radius);
            checkThreshold(Direction.TOP, position.y - radius);
            checkThreshold(Direction.BOTTOM, parent.height - (position.y + radius));

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
    }

    void think() {
        acceleration = brain.react();
    }

    void move() {
        if (!dead) {
            velocity.add(acceleration);
            velocity.limit(3);
            position.add(velocity);
        }
    }

    void show() {
        parent.fill(color);
        parent.ellipse(position.x, position.y, radius * 2, radius * 2);
    }

    void checkForCollisions() {
        if (!dead) {
            boolean collisionWithLeftBorder = position.x - radius <= 0;
            boolean collisionWithRightBorder = position.x + radius >= parent.width;
            boolean collisionWithTopBorder = position.y - radius <= 0;
            boolean collisionWithBottomBorder = position.y + radius >= parent.height;
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
                PApplet.println("Obstacle is too close to the " + direction + " The distance is " + distance);
                brain.setInputNeuron(direction.getInputNeuron(), distanceXThreshold / distance);
            }
        } else {
            if (distance > 0 && distance < distanceYThreshold) {
                PApplet.println("Obstacle is too close to the " + direction + " The distance is " + distance);
                brain.setInputNeuron(direction.getInputNeuron(), distanceYThreshold / distance);
            }
        }
    }
}
