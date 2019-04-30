package com.rmw.machinelearning;

import processing.core.PApplet;

abstract class CircularObject extends ScreenObject {

    private float radius = 10;

    CircularObject(final PApplet pApplet) {
        super(pApplet);
    }

    void setRadius(final int radius) {
        this.radius = radius;
    }

    float getRadius() {
        return radius;
    }

    @Override
    void show() {
        getPApplet().fill(getColour().v1, getColour().v2, getColour().v3);
        getPApplet().ellipse(getPosition().x, getPosition().y, radius * 2, radius * 2);
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
