package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

import static com.rmw.machinelearning.Configuration.PLAYER_COLOR;
import static com.rmw.machinelearning.Configuration.PLAYER_SPEED_LIMIT;
import static com.rmw.machinelearning.Configuration.PLAYER_START_POSITION;

class Player extends CircularObject {

    private final List<ScreenObject> obstacles;
    private final PVector velocity = new PVector();
    private boolean dead;

    Player(final PApplet p, final List<ScreenObject> obstacles) {
        super(p);
        this.obstacles = obstacles;
        reset();
    }

    void setVelocity(final float x, final float y) {
        velocity.set(x, y);
    }

    PVector getVelocity() {
        return velocity;
    }

    @Override
    void reset() {
        getPosition().set(PLAYER_START_POSITION.x, PLAYER_START_POSITION.y);
        velocity.set(0, 0);
        dead = false;
        setColour(PLAYER_COLOR.v1, PLAYER_COLOR.v2, PLAYER_COLOR.v3);
    }

    @Override
    void update() {
        if (!dead) {
            checkForCollisions();
            move();
        }
        super.update();
    }

    private void checkForCollisions() {
        final boolean isColliding = obstacles
                .stream()
                .anyMatch(obstacle -> {
                    final boolean xAxisCollision = getRightBorder() >= obstacle.getLeftBorder()
                                    && getLeftBorder() <= obstacle.getRightBorder();
                    final boolean yAxisCollision = getBottomBorder() >= obstacle.getTopBorder()
                                    && getTopBorder() <= obstacle.getBottomBorder();
                    return xAxisCollision && yAxisCollision;
                });
        if (isColliding) {
            dead = true;
        }
    }

    void move() {
        velocity.limit(PLAYER_SPEED_LIMIT);
        getPosition().add(velocity);
    }

    void stop() {
        setVelocity(0, 0);
    }

    boolean isDead() {
        return dead;
    }

}
