package com.rmw.machinelearning;

import processing.core.PApplet;

import static com.rmw.machinelearning.Configuration.HEIGHT;
import static com.rmw.machinelearning.Configuration.WIDTH;

public class Main extends PApplet {

    private final Obstacles obstacles = Obstacles.getInstance(this);
    private final Population population = new Population(this);
    private final EvilAI evilAI = new EvilAI(this);

    public static void main(final String[] args) {
        PApplet.main("com.rmw.machinelearning.Main", args);
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void setup() {
        obstacles.setEvilAI(evilAI);
    }

    public void draw() {
        background(0, 0, 255);
        obstacles.update();
        population.update();
    }

    public void keyPressed() {
        if (keyCode == UP) {
            evilAI.changeAcceleration(0, -1);
        }
        if (keyCode == DOWN) {
            evilAI.changeAcceleration(0, 1);
        }
        if (keyCode == LEFT) {
            evilAI.changeAcceleration(-1, 0);
        }
        if (keyCode == RIGHT) {
            evilAI.changeAcceleration(1, 0);
        }
    }

}
