package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.List;

import static java.text.MessageFormat.format;

class Population {

    private static final String NEW_GENERATION_READY = "Generation {0} is ready and contains {1} players";
    private static final String EACH_PLAYER_ALLOWED_STEPS = "Each player has {0} steps allowed to make";

    private final GeneticAlgorithm geneticAlgorithm;
    private int allowedSteps = 100;
    private List<Player> players;
    private int generationCounter;
    private int currentUpdate;

    Population(final PApplet pApplet) {
        geneticAlgorithm = new GeneticAlgorithm(pApplet);
        players = geneticAlgorithm.getInitialPopulation();
    }

    void update() {
        players.forEach(player -> {
            player.update();
        });
        // recreate the population if necessary (all are dead or ran out of steps)
        if (currentUpdate > allowedSteps || players.stream().allMatch(Player::isDead)) {
            currentUpdate = 0;
            generationCounter++;

            //each 5 generation, increase allowed steps amount by 50
            if (generationCounter % 5 == 0) {
                allowedSteps += 50;
            }

            players = geneticAlgorithm.getNextPopulation();
            PApplet.println(format(NEW_GENERATION_READY, generationCounter, players.size()));
            PApplet.println(format(EACH_PLAYER_ALLOWED_STEPS, allowedSteps));
        } else {
            currentUpdate++;
        }
    }

}
