package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.List;

class Population {

    private GeneticAlgorithm geneticAlgorithm;
    private List<Player> players;
    private int generationCounter = 0;
    private int counter = 1000;

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
        if (counter > 0) {
            counter--;
        } else {
            counter = 1000;
            generationCounter++;
            players = geneticAlgorithm.getNextPopulation();
            PApplet.println("New generation is ready and contains " + players.size() + " players");
            PApplet.println("Generation " + generationCounter + ". Ready, Steady, GO!");
        }
    }

}
