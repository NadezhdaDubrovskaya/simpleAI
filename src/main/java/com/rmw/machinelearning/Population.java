package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.List;

import static java.text.MessageFormat.format;

class Population {

    private static final String NEW_GENERATION_READY = "Generation {0} is ready and contains {1} computerPlayers";
    private static final int MAX_UPDATES = 5000;

    private final Obstacles obstacles;
    private final GeneticAlgorithm geneticAlgorithm;
    private final List<ComputerPlayer> computerPlayers;
    private int generationCounter;
    private int currentUpdate;

    Population(final PApplet pApplet) {
        geneticAlgorithm = new GeneticAlgorithm(pApplet);
        computerPlayers = geneticAlgorithm.getInitialPopulation();
        obstacles = Obstacles.getInstance(pApplet);
    }

    void update() {
        computerPlayers.forEach(ComputerPlayer::update);
        // recreate the population if necessary (all are dead or ran out of steps)
        if (computerPlayers.stream().allMatch(ComputerPlayer::isDead) || currentUpdate == MAX_UPDATES) {
            // when AIs start doing good - introduce new enemy :)
            if (currentUpdate == MAX_UPDATES) {
                obstacles.addEvilAI();
            }
            generationCounter++;
            currentUpdate = 0;
            geneticAlgorithm.getNextPopulation();
            obstacles.getObstacles().forEach(ScreenObject::reset);
            PApplet.println(format(NEW_GENERATION_READY, generationCounter, computerPlayers.size()));
        }
        currentUpdate++;
    }

}
