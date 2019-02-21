package com.rmw.machinelearning;

import processing.core.PApplet;

abstract class RectangularObject extends ScreenObject {

    int width;
    int height;

    RectangularObject(final PApplet pApplet) {
        super(pApplet);
    }

    @Override
    void show() {
        pApplet.fill(colour.v1, colour.v2, colour.v3);
        pApplet.rect(getPosition().x, getPosition().y, width, height);
    }

    @Override
    float getTopBorder() {
        return getPosition().y;
    }

    @Override
    float getBottomBorder() {
        return getPosition().y + height;
    }

    @Override
    float getLeftBorder() {
        return getPosition().x;
    }

    @Override
    float getRightBorder() {
        return getPosition().x + width;
    }

}
