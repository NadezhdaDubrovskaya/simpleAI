package com.rmw.machinelearning;

import processing.core.PApplet;

abstract class CircularObject extends ScreenObject {

    int radius = 10;

    CircularObject(final PApplet pApplet) {
        super(pApplet);
    }

    @Override
    void show() {
        pApplet.fill(colour.v1, colour.v2, colour.v3);
        pApplet.ellipse(getPosition().x, getPosition().y, radius * 2, radius * 2);
    }

    @Override
    float getTopBorder() {
        return getPosition().y - radius;
    }

    @Override
    float getBottomBorder() {
        return getPosition().y + radius;
    }

    @Override
    float getLeftBorder() {
        return getPosition().x - radius;
    }

    @Override
    float getRightBorder() {
        return getPosition().x + radius;
    }
}
