package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

import static com.rmw.machinelearning.Configuration.Colour;

abstract class ScreenObject {

    final PApplet pApplet;
    private final PVector position = new PVector();
    Colour colour = new Colour(0, 0, 0);

    ScreenObject(final PApplet pApplet) {
        this.pApplet = pApplet;
    }

    PVector getPosition() {
        return position;
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
