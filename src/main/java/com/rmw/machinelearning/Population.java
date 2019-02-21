package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.List;

import static java.text.MessageFormat.format;

class Population {

    private static final String NEW_GENERATION_READY = "Generation {0} is ready and contains {1} players";
    private static final String EACH_PLAYER_ALLOWED_STEPS = "Each player has {0} steps allowed to make";

    private final GeneticAlgorithm geneticAlgorithm;
    private List<Player> players;
    private int generationCounter;
    private int currentUpdate;

    Population(final PApplet pApplet) {
        geneticAlgorithm = new GeneticAlgorithm(pApplet);
        players = geneticAlgorithm.getInitialPopulation();
    }

    void update() {
        players.forEach(Player::update);
        // recreate the population if necessary (all are dead or ran out of steps)
        if (players.stream().allMatch(Player::isDead)) {
            currentUpdate = 0;
            generationCounter++;

            players = geneticAlgorithm.getNextPopulation();
            PApplet.println(format(NEW_GENERATION_READY, generationCounter, players.size()));
        } else {
            currentUpdate++;
        }
    }

}
