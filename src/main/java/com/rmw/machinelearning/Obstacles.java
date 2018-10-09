package com.rmw.machinelearning;

import java.util.ArrayList;
import java.util.List;

class Obstacles {

    private List<Wall> walls = new ArrayList<>();
    private static Obstacles instance;

    private Obstacles() {
        setupWalls();
    }

    static Obstacles getInstance() {
        if (instance == null) {
            instance = new Obstacles();
        }
        return instance;
    }

    List<Wall> getObstacles() {
        return walls;
    }

    private void setupWalls() {

    }

}
