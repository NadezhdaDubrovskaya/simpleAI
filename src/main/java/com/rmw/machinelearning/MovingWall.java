package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

import static com.rmw.machinelearning.Configuration.HEIGHT;
import static com.rmw.machinelearning.Configuration.WIDTH;
import static com.rmw.machinelearning.State.DONE;
import static com.rmw.machinelearning.State.IDLE;
import static com.rmw.machinelearning.State.MOVING;
import static com.rmw.machinelearning.State.MOVING_BACK;

class MovingWall extends Wall {

    private static final int SPEED = 2;
    private final Direction direction;
    private final int initialX;
    private final int initialY;
    private final PVector velocity = new PVector();
    private State state = IDLE;

    MovingWall(final PApplet p, final int x, final int y, final Direction direction, final int width, final int height) {
        super(p, x, y, width, height);
        initialX = x;
        initialY = y;
        this.direction = direction;
        setVelocity();
    }

    void reset() {
        state = IDLE;
        getPosition().set(initialX, initialY);
        setVelocity();
    }

    void start() {
        state = MOVING;
    }

    State getState() {
        return state;
    }

    @Override
    void update() {
        move();
        show();
    }

    private void move() {
        if (state == IDLE || state == DONE) {
            return;
        }
        getPosition().add(velocity);
        changeDirectionIfNecessary();
    }

    private void setVelocity() {
        switch (direction) {
            case RIGHT:
                velocity.set(SPEED, 0);
                break;
            case LEFT:
                velocity.set(-SPEED, 0);
                break;
            case BOTTOM:
                velocity.set(0, SPEED);
                break;
            case TOP:
                velocity.set(0, -SPEED);
                break;
        }
    }

    private void changeDirectionIfNecessary() {
        if (state == MOVING) {
            switch (direction) {
                case RIGHT:
                    if (getPosition().x >= WIDTH/2) {
                        turnBack();
                    }
                    break;
                case LEFT:
                    if (getPosition().x < WIDTH/2) {
                        turnBack();
                    }
                    break;
                case BOTTOM:
                    if (getPosition().y >= HEIGHT/2) {
                        turnBack();
                    }
                    break;
                case TOP:
                    if (getPosition().y < HEIGHT/2) {
                        turnBack();
                    }
                    break;
            }
        }
        if (state == MOVING_BACK && getPosition().x == initialX && getPosition().y == initialY) {
            state = DONE;
        }
    }

    private void turnBack() {
        velocity.mult(-1);
        state = MOVING_BACK;
    }
}
