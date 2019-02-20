package com.rmw.machinelearning;

import processing.core.PVector;

class Configuration {

    static final int WIDTH = 800;
    static final int HEIGHT = 800;

    static final int AMOUNT_OF_PLAYERS = 5;

    static final PVector PLAYER_START_POSITION = new PVector(50, 50);
    static final int PLAYER_RADIUS = 10;
    static final Colour PLAYER_COLOR = new Colour(0, 255, 0);
    static final Colour EVIL_AI_COLOUR = new Colour(255, 0, 0);
    static final int PLAYER_SPEED_LIMIT = 5;

    static final int DISTANCE_THRESHOLD = 10;

    static final int AMOUNT_OF_INPUT_NEURONS = 5;
    static final int AMOUNT_OF_HIDDEN_NEURONS = 5;
    static final int AMOUNT_OF_OUTPUT_NEURONS = 2;

    //genetic algorithm
    static final int BREED_TOP_X_PLAYERS = 2;
    static final int AMOUNT_OF_MUTATED_BABIES_IN_THE_POPULATION = 250;


    static class Colour {

        final int v1;
        final int v2;
        final int v3;

        Colour(final int v1, final int v2, final int v3) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
        }
    }
}
