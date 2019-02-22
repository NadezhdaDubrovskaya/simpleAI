package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

class Obstacles {

    private static Obstacles instance;
    private final PApplet pApplet;
    private final List<ScreenObject> screenObjects = new ArrayList<>();

    private Obstacles(final PApplet pApplet) {
        this.pApplet = pApplet;
        setupWalls();
    }

    static Obstacles getInstance(final PApplet pApplet) {
        if (instance == null) {
            instance = new Obstacles(pApplet);
        }
        return instance;
    }

    List<ScreenObject> getObstacles() {
        return screenObjects;
    }

    void update() {
        screenObjects.forEach(ScreenObject::update);
    }

    void addEvilAI() {
        screenObjects.add(new EvilAI(pApplet));
    }

    private void setupWalls() {
        screenObjects.add(new Wall(pApplet, 0, 300, 50, 50));
        screenObjects.add(new Wall(pApplet, 150, 400, 50, 50));
        screenObjects.add(new Wall(pApplet, 50, 550, 50, 50));
        screenObjects.add(new Wall(pApplet, 100, 500, 50, 50));
        screenObjects.add(new Wall(pApplet, 200, 0, 50, 50));
        screenObjects.add(new Wall(pApplet, 300, 100, 50, 200));
        screenObjects.add(new Wall(pApplet, 400, 0, 50, 50));
        screenObjects.add(new Wall(pApplet, 400, 300, 50, 50));
        screenObjects.add(new Wall(pApplet, 500, 500, 100, 50));
        screenObjects.add(new Wall(pApplet, 500, 0, 50, 50));
        screenObjects.add(new Wall(pApplet, 600, 0, 50, 150));
        screenObjects.add(new Wall(pApplet, 700, 0, 50, 50));
        screenObjects.add(new Wall(pApplet, 700, 300, 50, 50));
    }

}
