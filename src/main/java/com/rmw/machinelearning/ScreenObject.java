package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

import static com.rmw.machinelearning.Configuration.Colour;

abstract class ScreenObject {

    private final PApplet pApplet;
    private final PVector position = new PVector();
    private Colour colour = new Colour(0, 0, 0);

    ScreenObject(final PApplet pApplet) {
        this.pApplet = pApplet;
    }

    PApplet getPApplet() {
        return pApplet;
    }

    void setPosition(final float x, final float y) {
        position.x = x;
        position.y = y;
    }

    PVector getPosition() {
        return position;
    }

    Colour getColour() {
        return colour;
    }

    void setColour(final Colour colour) {
        this.colour = colour;
    }

    void setColour(final int v1, final int v2, final int v3) {
        colour.v1 = v1;
        colour.v2 = v2;
        colour.v3 = v3;
    }

    void reset() {
        //do nothing by default
    }

    void update() {
        show();
    }

    abstract void show();

    abstract float getTopBorder();

    abstract float getBottomBorder();

    abstract float getLeftBorder();

    abstract float getRightBorder();
}
