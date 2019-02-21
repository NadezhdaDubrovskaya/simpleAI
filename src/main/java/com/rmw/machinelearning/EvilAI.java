package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

import static com.rmw.machinelearning.Configuration.HEIGHT;
import static com.rmw.machinelearning.Configuration.PLAYER_SPEED_LIMIT;
import static com.rmw.machinelearning.Configuration.WIDTH;
import static com.rmw.machinelearning.Utility.maybeYes;

class EvilAI extends CircularObject {

    private final PVector acceleration;
    private final PVector velocity;

    EvilAI(final PApplet pApplet) {
        super(pApplet);
        getPosition().set(pApplet.random(0, WIDTH), pApplet.random(0, HEIGHT));
        velocity = new PVector(0, 0);
        acceleration = new PVector(0, 0);
    }

    @Override
    void update() {
        move();
        show();
    }

    private void move() {
        //TODO refactor
        if (maybeYes()) {
            acceleration.set(pApplet.random(-1, 1), pApplet.random(-1, 1));
        }
        velocity.add(acceleration);
        velocity.limit(3);
        final PVector assumedPosition = new PVector().set(getPosition());
        assumedPosition.add(velocity);
        if (pApplet.width - (assumedPosition.x + radius) > 0
                && assumedPosition.x - radius > 0
                && assumedPosition.y - radius > 0
                && pApplet.height - (assumedPosition.y + radius) > 0) {
            getPosition().add(velocity);
        }
    }

    void changeAcceleration(final int x, final int y) {
        acceleration.set(x, y);
    }

}
