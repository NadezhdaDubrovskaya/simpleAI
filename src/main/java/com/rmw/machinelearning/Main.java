package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.List;

public class Main extends PApplet {

    private Obstacles obstacles = Obstacles.getInstance(this);
    private Population population = new Population(this);

    public static void main(String[] args) {
        PApplet.main("com.rmw.machinelearning.Main", args);
    }

    public void settings() {
        size(800, 800);
    }

    public void setup() {
    }

    public void draw() {
        background(0, 0, 255);
        obstacles.update();
        population.update();
    }

}
