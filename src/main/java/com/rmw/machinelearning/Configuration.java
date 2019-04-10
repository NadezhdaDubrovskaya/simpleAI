package com.rmw.machinelearning;

import processing.core.PVector;

import java.util.Arrays;
import java.util.List;

class Configuration {

    /**
     * If false then it will behave as a regular game and player will be given an opportunity to run and avoid AIs
     * If true then it means the game is in the mode of teaching AIs
     */
    static final boolean IS_AI_MODE = false;

    static final int WIDTH = 800;
    static final int HEIGHT = 800;

    //genetic algorithm
    static final int AMOUNT_OF_PLAYERS = 500; //should be even number and at least 4
    static final int AMOUNT_OF_MUTATED_BABIES_IN_THE_POPULATION = AMOUNT_OF_PLAYERS / 10;

    static final PVector PLAYER_START_POSITION = new PVector(400, 400);
    static final Colour PLAYER_COLOR = new Colour(255, 255, 0);
    static final Colour BEST_PLAYER_COLOR = new Colour(0, 255, 0);
    static final int PLAYER_SPEED_LIMIT = 5;

    static final int DISTANCE_THRESHOLD = 50;
    static final List<Integer> HIDDEN_LAYERS_CONFIGURATION = Arrays.asList(3);
    static final int AMOUNT_OF_INPUT_NEURONS = 4; // do not change
    static final int AMOUNT_OF_OUTPUT_NEURONS = 2; //do not change
    static int AMOUNT_OF_CONNECTIONS;

    static {
        // main connections
        if (!HIDDEN_LAYERS_CONFIGURATION.isEmpty()) {
            AMOUNT_OF_CONNECTIONS = AMOUNT_OF_INPUT_NEURONS * HIDDEN_LAYERS_CONFIGURATION.get(0);
            if (HIDDEN_LAYERS_CONFIGURATION.size() > 1) {
                for (int i = 0; i < HIDDEN_LAYERS_CONFIGURATION.size() - 1; i++) {
                    AMOUNT_OF_CONNECTIONS += HIDDEN_LAYERS_CONFIGURATION.get(i) * HIDDEN_LAYERS_CONFIGURATION.get(i + 1);
                }
            }
            AMOUNT_OF_CONNECTIONS += HIDDEN_LAYERS_CONFIGURATION.get(HIDDEN_LAYERS_CONFIGURATION.size() - 1) * AMOUNT_OF_OUTPUT_NEURONS;
        } else {
            AMOUNT_OF_CONNECTIONS = AMOUNT_OF_INPUT_NEURONS * AMOUNT_OF_OUTPUT_NEURONS;
        }
        // connections with bias neuron
        AMOUNT_OF_CONNECTIONS += AMOUNT_OF_OUTPUT_NEURONS;
        HIDDEN_LAYERS_CONFIGURATION.forEach(amount -> AMOUNT_OF_CONNECTIONS += amount);
    }

    static class Colour {

        int v1;
        int v2;
        int v3;

        Colour(final int v1, final int v2, final int v3) {
            this.v1 = v1;
            this.v2 = v2;
            this.v3 = v3;
        }
    }
}
