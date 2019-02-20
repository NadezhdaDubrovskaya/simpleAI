package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

import static com.rmw.machinelearning.Configuration.EVIL_AI_COLOUR;
import static com.rmw.machinelearning.Configuration.PLAYER_RADIUS;
import static com.rmw.machinelearning.Configuration.PLAYER_SPEED_LIMIT;

class EvilAI extends ScreenObject {

    private final PApplet pApplet;

    private final PVector acceleration;
    private final PVector velocity;

    EvilAI(final PApplet pApplet) {
        this.pApplet = pApplet;
        position = new PVector(150, 150);
        velocity = new PVector(0, 0);
        acceleration = new PVector(0, 0);
    }

    @Override
    void update() {
        move();
        show();
    }

    //TODO move into separate class getTop/Bottom/Left/Right borders
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

    private void move() {
        velocity.add(acceleration);
        velocity.limit(PLAYER_SPEED_LIMIT);
        position.add(velocity);
    }

    private void show() {
        pApplet.fill(EVIL_AI_COLOUR.v1, EVIL_AI_COLOUR.v2, EVIL_AI_COLOUR.v3);
        pApplet.ellipse(position.x, position.y, PLAYER_RADIUS * 2, PLAYER_RADIUS * 2);
    }

    void changeAcceleration(final int x, final int y) {
        acceleration.set(x, y);
    }

}
