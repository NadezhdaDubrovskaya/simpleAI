package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

import static com.rmw.machinelearning.Configuration.PLAYER_COLOR;
import static com.rmw.machinelearning.Configuration.PLAYER_SPEED_LIMIT;
import static com.rmw.machinelearning.Configuration.PLAYER_START_POSITION;

class Player extends CircularObject {

    private final PVector velocity = new PVector();
    private boolean dead;

    Player(final PApplet p) {
        super(p);
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
        setDead(false);
        setColour(PLAYER_COLOR);
    }

    @Override
    void update() {
        move();
        show();
    }

    void move() {
        if (!dead) {
            velocity.limit(PLAYER_SPEED_LIMIT);
            getPosition().add(velocity);
        }
    }

    void stop() {
        setVelocity(0,0);
    }

    boolean isDead() {
        return dead;
    }

    void setDead(final boolean dead) {
        this.dead = dead;
    }
}
