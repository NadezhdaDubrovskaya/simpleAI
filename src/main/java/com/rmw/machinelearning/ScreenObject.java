package com.rmw.machinelearning;

import processing.core.PVector;

abstract class ScreenObject {

    PVector position = new PVector();

    abstract void update();

    abstract float getTopBorder();

    abstract float getBottomBorder();

    abstract float getLeftBorder();

    abstract float getRightBorder();
}
