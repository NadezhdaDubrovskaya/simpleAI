package com.rmw.machinelearning;

import processing.core.PApplet;

class Wall extends RectangularObject {

    Wall(final PApplet p, final int x, final int y, final int width, final int height) {
        super(p);
        getPosition().set(x, y);
        this.width = width;
        this.height = height;
    }

}
