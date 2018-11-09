package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.List;

class Population {

    private int allowedSteps = 100;
    private GeneticAlgorithm geneticAlgorithm;
    private List<Player> players;
    private int generationCounter = 0;
    private int currentUpdate = 0;

    Population(PApplet pApplet) {
        geneticAlgorithm = new GeneticAlgorithm(pApplet);
        players = geneticAlgorithm.getInitialPopulation();
    }

    void update() {
        for (Player player : players) {
                player.look();
                player.think();
                player.move();
                player.show();
        }
        // recreate the population if necessary (all are dead or ran out of steps)
        if (currentUpdate > allowedSteps || players.stream().allMatch(Player::isDead)) {
            currentUpdate = 0;
            generationCounter++;

            //each 5 generation, increase allowed steps amount by 50
            if (generationCounter%5 == 0) {
                allowedSteps += 50;
            }

            players = geneticAlgorithm.getNextPopulation();
            PApplet.println("New generation is ready and contains " + players.size() + " players");
            PApplet.println("Generation " + generationCounter + ". Ready, Steady, GO!");
        } else {
            currentUpdate++;
        }
    }

}
