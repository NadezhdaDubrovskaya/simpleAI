package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.rmw.machinelearning.Configuration.AMOUNT_OF_HIDDEN_NEURONS;
import static com.rmw.machinelearning.Configuration.AMOUNT_OF_INPUT_NEURONS;
import static com.rmw.machinelearning.Configuration.AMOUNT_OF_MUTATED_BABIES_IN_THE_POPULATION;
import static com.rmw.machinelearning.Configuration.AMOUNT_OF_OUTPUT_NEURONS;
import static com.rmw.machinelearning.Configuration.AMOUNT_OF_PLAYERS;
import static com.rmw.machinelearning.Configuration.Colour;
import static com.rmw.machinelearning.Utility.maybeYes;
import static java.lang.Math.round;

class GeneticAlgorithm {

    private final PApplet pApplet;
    private final List<Player> players = new ArrayList<>();
    private int bestScoreSoFar;

    GeneticAlgorithm(final PApplet p) {
        pApplet = p;
    }

    List<Player> getInitialPopulation() {
        for (int i = 0; i < AMOUNT_OF_PLAYERS; i++) {
            players.add(new Player(pApplet, generateRandomWeights()));
        }
        return players;
    }

    void getNextPopulation() {

        sortPlayerBasedOnTheirFitnessScore();
        // in case the whole generation failed (instant death) - give them another chance
        if (players.get(0).getFitnessScore() == 0) {
            restartCurrentGeneration();
            return;

        }
        // else proceed with the algorithm
        getStatistics();
        breedTheBestOnes();
        applyMutations();
        getReadyForTheNextRound();

    }

    private void getReadyForTheNextRound() {
        players.forEach(Player::reset);
        int greenColorIntensity = 255;
        for (final Player player : players) {
            player.colour = new Colour(150, greenColorIntensity, 0);
            if (greenColorIntensity > 0) {
                greenColorIntensity--;
            }
        }
    }

    private void sortPlayerBasedOnTheirFitnessScore() {
        players.forEach(Player::calculateFitness);
        players.sort(Comparator.comparing(Player::getFitnessScore).reversed());
    }

    private void getStatistics() {
        bestScoreSoFar = players.get(0).getFitnessScore();
        final int totalFitnessScore = players.stream().mapToInt(Player::getFitnessScore).sum();
        PApplet.println("Total fitness score of the whole generation: " + totalFitnessScore);
        PApplet.println("Best score so far: " + bestScoreSoFar);
        PApplet.println("Best genes: " + players.get(0).getWeights());
    }

    /**
     * We do not create new player here but rather modify the genes of the existing ones
     */
    private void breedTheBestOnes() {
        final int middleIndex = players.size() / 4 + 1;
        for (int i = 0, j = middleIndex; i < middleIndex && j < players.size(); i++, j++) {
            final List<Float> newGenes = getBabyGenes(players.get(i), players.get(i + 1));
            players.get(j).changeWeights(newGenes);
        }
    }

    private List<Float> getBabyGenes(final Player parent1, final Player parent2) {
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
        final int randomIndex = round(pApplet.random(players.size() >> 2, players.size() - 1));
        final List<Float> genes = players.get(randomIndex).getWeights();
        for (int i = 0; i < genes.size(); i++) {
            if (maybeYes()) {
                genes.set(i, generateRandomWeight());
            }
        }
    }

    private void restartCurrentGeneration() {
        PApplet.println("Oops, bad luck. Let's try again");
        final List<Player> copy = new ArrayList<>(players);
        players.clear();
        copy.forEach(player -> players.add(new Player(pApplet, player.getWeights())));
        PApplet.println("Best score so far: " + bestScoreSoFar);
    }

    private List<Float> generateRandomWeights() {
        final List<Float> result = new ArrayList<>();
        int weightsAmount = AMOUNT_OF_INPUT_NEURONS * AMOUNT_OF_HIDDEN_NEURONS;
        weightsAmount += AMOUNT_OF_HIDDEN_NEURONS * AMOUNT_OF_OUTPUT_NEURONS;
        for (int i = 0; i < weightsAmount; i++) {
            result.add(generateRandomWeight());
        }
        return result;
    }

    private float generateRandomWeight() {
        return round(pApplet.random(-1, 1));
    }
}
