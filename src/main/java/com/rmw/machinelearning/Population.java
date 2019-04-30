package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.List;

import static java.text.MessageFormat.format;

class Population {

    private static final String NEW_GENERATION_READY = "Generation {0} is ready and contains {1} computerPlayers";
    private static final int MAX_UPDATES = 5000;

    private final List<ComputerPlayer> computerPlayers;
    private final List<ScreenObject> obstacles;
    private final GeneticAlgorithm geneticAlgorithm;

    private int generationCounter;
    private int currentUpdate;

    Population(final PApplet pApplet, final List<ScreenObject> obstacles) {
        geneticAlgorithm = new GeneticAlgorithm(pApplet, obstacles);
        computerPlayers = geneticAlgorithm.getInitialPopulation();
        this.obstacles = obstacles;
    }

    void update() {
        computerPlayers.forEach(ComputerPlayer::update);
        // recreate the population if necessary (all are dead or ran out of steps)
        if (computerPlayers.stream().allMatch(ComputerPlayer::isDead) || currentUpdate == MAX_UPDATES) {
            generationCounter++;
            currentUpdate = 0;
            geneticAlgorithm.getNextPopulation();
            obstacles.forEach(ScreenObject::reset);
            PApplet.println(format(NEW_GENERATION_READY, generationCounter, computerPlayers.size()));
        }
        currentUpdate++;
    }

}
