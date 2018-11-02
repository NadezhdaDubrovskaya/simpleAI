package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

class Obstacles {

    private PApplet pApplet;
    private List<Wall> walls = new ArrayList<>();
    private static Obstacles instance;

    private Obstacles(PApplet pApplet) {
        this.pApplet = pApplet;
        setupWalls();
    }

    static Obstacles getInstance(PApplet pApplet) {
        if (instance == null) {
            instance = new Obstacles(pApplet);
        }
        return instance;
    }

    List<Wall> getObstacles() {
        return walls;
    }

    void update() {
        for (Wall wall : walls) {
            wall.show();
        }
    }

    private void setupWalls() {
        walls.add(new Wall(pApplet, 600, 100, 50, 200));
        walls.add(new Wall(pApplet, 100, 100, 50, 200));
        walls.add(new Wall(pApplet, 200, 500, 50, 200));
    }

}
