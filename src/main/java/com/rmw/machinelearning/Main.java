package com.rmw.machinelearning;

import processing.core.PApplet;

import static com.rmw.machinelearning.Configuration.AMOUNT_OF_PLAYERS;
import static com.rmw.machinelearning.Configuration.HEIGHT;
import static com.rmw.machinelearning.Configuration.IS_AI_MODE;
import static com.rmw.machinelearning.Configuration.WIDTH;

public class Main extends PApplet {

    private final Obstacles obstacles = Obstacles.getInstance(this);
    private Population population = new Population(this);

    public static void main(final String[] args) {
        PApplet.main("com.rmw.machinelearning.Main", args);
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup() {
        if (IS_AI_MODE && AMOUNT_OF_PLAYERS < 4) {
            throw new IllegalArgumentException("Amount of players can't be less than 4");
        }
    }

    @Override
    public void draw() {
        background(0, 0, 255);
        obstacles.update();
        if (IS_AI_MODE) {
            population.update();
        }
    }
}

