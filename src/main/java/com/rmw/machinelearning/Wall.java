package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

class Wall extends ScreenObject {

    private static final int COLOUR = 150;
    private final PApplet parent;
    private final int width;
    private final int height;

    Wall(final PApplet p, final int x, final int y, final int width, final int height) {
        position = new PVector(x, y);
        this.width = width;
        this.height = height;
        parent = p;
    }

    @Override
    void update() {
        show();
    }

    @Override
    float getTopBorder() {
        return position.y;
    }

    @Override
    float getBottomBorder() {
        return position.y + height;
    }

    @Override
    float getLeftBorder() {
        return position.x;
    }

    @Override
    float getRightBorder() {
        return position.x + width;
    }

    private void show() {
        parent.fill(COLOUR);
        parent.rect(position.x, position.y, width, height);
    }

}
