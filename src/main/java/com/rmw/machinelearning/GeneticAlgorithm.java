package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

import static com.rmw.machinelearning.Configuration.AMOUNT_OF_CONNECTIONS;
import static com.rmw.machinelearning.Configuration.AMOUNT_OF_MUTATED_BABIES_IN_THE_POPULATION;
import static com.rmw.machinelearning.Configuration.AMOUNT_OF_PLAYERS;
import static com.rmw.machinelearning.Utility.maybeYes;
import static java.lang.Math.round;
import static java.util.Comparator.comparing;

class GeneticAlgorithm {

    private final PApplet pApplet;
    private final List<ScreenObject> obstacles;
    private final List<ComputerPlayer> computerPlayers = new ArrayList<>();
    private int bestScoreSoFar;

    GeneticAlgorithm(final PApplet p, final List<ScreenObject> obstacles) {
        pApplet = p;
        this.obstacles = obstacles;
    }

    List<ComputerPlayer> getInitialPopulation() {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < AMOUNT_OF_PLAYERS; i++) {
            final ComputerPlayer player = new ComputerPlayer(pApplet, obstacles);
            final Brain brain = new Brain(generateRandomWeights());
            player.setBrain(brain);
            computerPlayers.add(player);
        }
        long stopTime = System.currentTimeMillis();
        long elapsedTime = stopTime - startTime;
        PApplet.println("Initial population had been created in " + elapsedTime + " ms");
        return computerPlayers;
    }

    void getNextPopulation() {
        sortPlayerBasedOnTheirFitnessScore();
        // in case the whole generation failed (instant death) - give them another chance
        // this can happen if evil AI is located at the respawn location
        if (computerPlayers.get(0).getFitnessScore() == 0) {
            restartCurrentGeneration();
            return;
        }
        // else proceed with the algorithm
        getStatistics();
        breedTheBestOnes();
        applyMutations();
        getReadyForTheNextRound();
    }

    private void sortPlayerBasedOnTheirFitnessScore() {
        computerPlayers.forEach(ComputerPlayer::calculateFitness);
        computerPlayers.sort(comparing(ComputerPlayer::getFitnessScore).reversed());
    }

    private void getStatistics() {
        bestScoreSoFar = computerPlayers.get(0).getFitnessScore();
        final int totalFitnessScore = computerPlayers.stream().mapToInt(ComputerPlayer::getFitnessScore).sum();
        PApplet.println("Total fitness score of the whole generation: " + totalFitnessScore);
        PApplet.println("Best score so far: " + bestScoreSoFar);
        PApplet.println("Best genes: " + computerPlayers.get(0).getWeights());
        PApplet.println("Current best is " + computerPlayers.get(0));
    }

    private void breedTheBestOnes() {
        final int middleIndex = computerPlayers.size() / 4 + 1;
        for (int i = 0, j = middleIndex; i < middleIndex && j < computerPlayers.size(); i++, j++) {
            final List<Float> newGenes = getBabyGenes(computerPlayers.get(i), computerPlayers.get(i + 1));
            final Brain newBrain = new Brain(newGenes);
            computerPlayers.get(j).setBrain(newBrain);
        }
    }

    private List<Float> getBabyGenes(final ComputerPlayer parent1, final ComputerPlayer parent2) {
        //copy the genes but do not modify the parent ones
        final List<Float> genes1 = new ArrayList<>(parent1.getWeights());
        final List<Float> genes2 = new ArrayList<>(parent2.getWeights());
        chromosomalCrossover(genes1, genes2);
        return genes1;
    }

    private void chromosomalCrossover(final List<Float> parent1Genes, final List<Float> parent2Genes) {
        for (int i = 0; i < parent1Genes.size(); i++) {
            //replace random genes of parent1 with corresponding gene of parent2
            if (maybeYes()) {
                parent1Genes.set(i, parent2Genes.get(i));
            }
        }
    }

    private void applyMutations() {
        int babiesMutated = 0;
        while (babiesMutated < AMOUNT_OF_MUTATED_BABIES_IN_THE_POPULATION) {
            mutateBabies();
            babiesMutated++;
        }
        PApplet.println("Mutated: " + babiesMutated);
    }

    private void mutateBabies() {
        final int randomIndex = round(pApplet.random(computerPlayers.size() >> 2, computerPlayers.size() - 1));
        final List<Float> genes = computerPlayers.get(randomIndex).getWeights();
        for (int i = 0; i < genes.size(); i++) {
            if (maybeYes()) {
                genes.set(i, generateRandomWeight());
            }
        }
    }

    private void getReadyForTheNextRound() {
        computerPlayers.forEach(ComputerPlayer::reset);
        int greenColorIntensity = 255;
        for (final ComputerPlayer computerPlayer : computerPlayers) {
            computerPlayer.setColour(150, greenColorIntensity, 0);
            if (greenColorIntensity > 0) {
                greenColorIntensity--;
            }
        }
    }

    private void restartCurrentGeneration() {
        PApplet.println("Oops, bad luck. Let's try again");
        computerPlayers.forEach(ComputerPlayer::reset);
        PApplet.println("Best score so far: " + bestScoreSoFar);
    }

    private List<Float> generateRandomWeights() {
        final List<Float> result = new ArrayList<>();
        for (int i = 0; i < AMOUNT_OF_CONNECTIONS; i++) {
            result.add(generateRandomWeight());
        }
        return result;
    }

    private float generateRandomWeight() {
        return round(pApplet.random(-1, 1));
    }
}
