package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

class Wall {

    PVector position;
    int width;
    int height;
    private PApplet parent;

    Wall(PApplet p, int x, int y, int width, int height) {
        this.position = new PVector(x,y);
        this.width = width;
        this.height = height;
        this.parent = p;
    }

    void show() {
        parent.fill(150);
        parent.rect(position.x, position.y , width, height);
    }

}
