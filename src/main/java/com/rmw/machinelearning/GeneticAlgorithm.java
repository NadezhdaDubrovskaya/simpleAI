package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static com.rmw.machinelearning.Configuration.BEST_PLAYER_COLOR;
import static com.rmw.machinelearning.Utility.maybeYes;
import static java.lang.Math.round;

class GeneticAlgorithm {

    private final PApplet pApplet;
    private List<Player> players;
    private float bestScoreSoFar;

    GeneticAlgorithm(final PApplet p) {
        pApplet = p;
    }

    List<Player> getInitialPopulation() {
        players = new ArrayList<>();
        for (int i = 0; i < Configuration.AMOUNT_OF_PLAYERS; i++) {
            players.add(new Player(pApplet, generateRandomWeights()));
        }
        return players;
    }

    List<Player> getNextPopulation() {

        sortPlayerBasedOnFitnessFunction();
        // in case the whole generation failed (instant death) - give them another chance
        if (players.get(0).getFitnessScore() == 0) {
            PApplet.println("Oops, bad luck. Let's try again");
            final List<Player> copy = new ArrayList<>(players);
            players.clear();
            copy.forEach(player -> players.add(new Player(pApplet, player.getWeights())));
            PApplet.println("Best score so far: " + bestScoreSoFar);
            return players;
        }
        // else proceed with the algorithm
        bestScoreSoFar = players.get(0).getFitnessScore();
        final List<Player> bestPlayers = new ArrayList<>(players.subList(0, Configuration.BREED_TOP_X_PLAYERS));
        final List<Float> bestGenes = bestPlayers.get(0).getWeights();
        final Player bestPlayer = new Player(pApplet, bestGenes);
        bestPlayer.colour = BEST_PLAYER_COLOR;
        PApplet.println("Best score so far: " + bestScoreSoFar);
        PApplet.println("Best genes: " + bestGenes);
        players.clear();

        while (players.size() < Configuration.AMOUNT_OF_PLAYERS) {
            final int randomIndex1 = getRandomIndex(Configuration.BREED_TOP_X_PLAYERS - 1);
            int randomIndex2 = getRandomIndex(Configuration.BREED_TOP_X_PLAYERS - 1);
            while (randomIndex1 == randomIndex2) {
                randomIndex2 = getRandomIndex(Configuration.BREED_TOP_X_PLAYERS - 1);
            }
            getBabies(bestPlayers.get(randomIndex1), bestPlayers.get(randomIndex2));
        }

        int babiesMutated = 0;
        final List<Integer> listOfAlreadyMutated = new ArrayList<>(Configuration.AMOUNT_OF_MUTATED_BABIES_IN_THE_POPULATION);
        while (babiesMutated < Configuration.AMOUNT_OF_MUTATED_BABIES_IN_THE_POPULATION) {
            mutateBabies(listOfAlreadyMutated);
            babiesMutated++;
        }
        PApplet.print("Mutated: " + babiesMutated);
        PApplet.print("Mutated indexes: " + listOfAlreadyMutated);

        players.add(bestPlayer);

        return players;
    }

    private void sortPlayerBasedOnFitnessFunction() {
        players.forEach(Player::calculateFitness);
        players.sort(Comparator.comparing(Player::getFitnessScore).reversed());
    }

    private void getBabies(final Player parent1, final Player parent2) {
        //copy the genes but do not modify the parent ones
        final List<Float> genes1 = new ArrayList<>(parent1.getWeights());
        final List<Float> genes2 = new ArrayList<>(parent2.getWeights());
        chromosomalCrossover(genes1, genes2);
        players.add(new Player(pApplet, genes1));
        players.add(new Player(pApplet, genes2));
    }

    private void mutateBabies(final List<Integer> listOfAlreadyMutated) {
        int randomIndex = getRandomIndex(players.size() - 1);
        while (listOfAlreadyMutated.contains(randomIndex)) {
            randomIndex = getRandomIndex(players.size() - 1);
        }
        listOfAlreadyMutated.add(randomIndex);
        final List<Float> genes = players.get(randomIndex).getWeights();
        for (int i = 0; i < genes.size(); i++) {
            if (maybeYes()) {
                genes.set(i, generateRandomWeight());
            }
        }
    }

    private void chromosomalCrossover(final List<Float> parent1Genes, final List<Float> parent2Genes) {
        for (int i = 0; i < parent1Genes.size(); i++) {
            //swap random genes
            if (maybeYes()) {
                final float temp = parent1Genes.get(i);
                parent1Genes.set(i, parent2Genes.get(i));
                parent2Genes.set(i, temp);
            }
        }
    }

    private List<Float> generateRandomWeights() {
        final List<Float> result = new ArrayList<>();
        int weightsAmount = Configuration.AMOUNT_OF_INPUT_NEURONS * Configuration.AMOUNT_OF_HIDDEN_NEURONS;
        weightsAmount += Configuration.AMOUNT_OF_HIDDEN_NEURONS * Configuration.AMOUNT_OF_OUTPUT_NEURONS;
        for (int i = 0; i < weightsAmount; i++) {
            result.add(generateRandomWeight());
        }
        return result;
    }

    private float generateRandomWeight() {
        return round(pApplet.random(-1, 1));
    }

    private int getRandomIndex(final int maxValue) {
        return (int) pApplet.random(maxValue);
    }
}
