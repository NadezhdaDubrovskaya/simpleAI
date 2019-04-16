package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

import static com.rmw.machinelearning.Configuration.HEIGHT;
import static com.rmw.machinelearning.Configuration.WIDTH;

class Obstacles {

    private static Obstacles instance;
    private final PApplet pApplet;
    private final List<ScreenObject> screenObjects = new ArrayList<>();

    private Obstacles(final PApplet pApplet) {
        this.pApplet = pApplet;
        //add screen borders
        screenObjects.add(new Wall(pApplet, 0, 0, WIDTH, 1));
        screenObjects.add(new Wall(pApplet, 0, 0, 1, HEIGHT));
        screenObjects.add(new Wall(pApplet, WIDTH, 0, 1, HEIGHT));
        screenObjects.add(new Wall(pApplet, 0, HEIGHT, WIDTH, 1));
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

    void addEvilAI(final Monster monster) {
        screenObjects.add(monster);
    }

    private void setupWalls() {
        screenObjects.add(new Wall(pApplet, 0, 0, 10, HEIGHT));
        screenObjects.add(new Wall(pApplet, 0, 0, WIDTH, 10));
        screenObjects.add(new Wall(pApplet, WIDTH-10, 0, 10, HEIGHT));
        screenObjects.add(new Wall(pApplet, 0, HEIGHT-10, WIDTH, 10));
        screenObjects.add(new Wall(pApplet, 100, 50, 200, 50));
        screenObjects.add(new Wall(pApplet, 100, 100, 50, 50));
        screenObjects.add(new Wall(pApplet, 200, 250, 50, 300));
        screenObjects.add(new Wall(pApplet, 250, 650, 50, 150));
        screenObjects.add(new Wall(pApplet, 300, 200, 250, 50));
        screenObjects.add(new Wall(pApplet, 300, 250, 50, 50));
        screenObjects.add(new Wall(pApplet, 350, 700, 50, 100));
        screenObjects.add(new Wall(pApplet, 400, 450, 50, 200));
        screenObjects.add(new Wall(pApplet, 500, 50, 250, 50));
        screenObjects.add(new Wall(pApplet, 500, 300, 50, 100));
        screenObjects.add(new Wall(pApplet, 500, 450, 150, 50));
        screenObjects.add(new Wall(pApplet, 500, 500, 50, 100));
        screenObjects.add(new Wall(pApplet, 500, 650, 50, 150));
        screenObjects.add(new Wall(pApplet, 600, 350, 50, 150));
        screenObjects.add(new Wall(pApplet, 650, 650, 100, 50));
        screenObjects.add(new Wall(pApplet, 700, 100, 50, 500));
    }

}
