package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Main extends PApplet {

    private GeneticAlgorithm geneticAlgorithm = new GeneticAlgorithm(this);
    private List<Wall> walls = Obstacles.getInstance().getObstacles();
    private List<Player> players;
    private int counter = 500; //how long to let players live

    public static void main(String[] args) {
        PApplet.main("com.rmw.machinelearning.Main", args);
    }

    public void settings() {
        size(800, 800);
    }

    public void setup() {
        players = geneticAlgorithm.getInitialPopulation();
        walls.add(new Wall(this, 600, 100, 50, 200));
        walls.add(new Wall(this, 100, 100, 50, 200));
        walls.add(new Wall(this, 200, 500, 50, 200));
    }

    public void draw() {
        background(0, 0, 255);
        for (Wall wall : walls) {
            wall.show();
        }
        for (Player player : players) {
            player.look();
            player.think();
            player.move();
            player.show();
            player.checkForCollisions();
        }
        if (counter > 0) {
            counter--;
        } else {
            players = geneticAlgorithm.getNextPopulation();
            counter = 1000;
        }
    }

}
