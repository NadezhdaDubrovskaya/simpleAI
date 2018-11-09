package com.rmw.machinelearning;

import processing.core.PVector;

class Configuration {

    static final int width = 800;
    static final int height = 800;

    static final int AMOUNT_OF_PLAYERS = 500;

    static final PVector PLAYER_START_POSITION = new PVector(50, 50);
    static final PVector GOAL_POSITION = new PVector(width - 50, height - 50);
    static final int PLAYER_RADIUS = 10;
    static final int PLAYER_COLOR = 255;
    static final int PLAYER_SPEED_LIMIT = 5;

    static final int DISTANCE_THRESHOLD = 80;

    static final int AMOUNT_OF_INPUT_NEURONS = 5;
    static final int AMOUNT_OF_HIDDEN_NEURONS = 5;
    static final int AMOUNT_OF_OUTPUT_NEURONS = 2;

    //genetic algorithm
    static final int BREED_TOP_X_PLAYERS = 10;
    static final int AMOUNT_OF_MUTATED_BABIES_IN_THE_POPULATION = 250;


}
