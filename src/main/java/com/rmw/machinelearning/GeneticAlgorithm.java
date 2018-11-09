package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class GeneticAlgorithm {

    private PApplet pApplet;
    private List<Player> players;

    GeneticAlgorithm(PApplet p) {
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
        List<Player> bestPlayers = new ArrayList<>(players.subList(0, Configuration.BREED_TOP_X_PLAYERS));
        List<Float> bestGenes = bestPlayers.get(0).exposeNeuronNetwork().getWeights();
        PApplet.println("Best player has fitness score of " + bestPlayers.get(0).getFitnessScore());
        PApplet.println("Best genes: " + bestGenes);
        players.clear();

        while (players.size() < Configuration.AMOUNT_OF_PLAYERS) {
            int randomIndex1 = getRandomIndex(Configuration.BREED_TOP_X_PLAYERS - 1);
            int randomIndex2 = getRandomIndex(Configuration.BREED_TOP_X_PLAYERS - 1);
            while (randomIndex1 == randomIndex2) {
                randomIndex2 = getRandomIndex(Configuration.BREED_TOP_X_PLAYERS - 1);
            }
            getBabies(bestPlayers.get(randomIndex1), bestPlayers.get(randomIndex2));
        }

        int babiesMutated = 0;
        List<Integer> listOfAlreadyMutated = new ArrayList<>(Configuration.AMOUNT_OF_MUTATED_BABIES_IN_THE_POPULATION);
        while (babiesMutated < Configuration.AMOUNT_OF_MUTATED_BABIES_IN_THE_POPULATION) {
            mutateBabies(listOfAlreadyMutated);
            babiesMutated++;
        }
        PApplet.print("Mutated: "  + babiesMutated);
        PApplet.print("Mutated indexes: " + listOfAlreadyMutated);

        players.add(new Player(pApplet, bestGenes));

        return players;
    }

    private void sortPlayerBasedOnFitnessFunction() {
        players.forEach(Player::calculateFitness);
        players.sort(Comparator.comparing(Player::getFitnessScore).reversed());
    }

    private void getBabies(Player parent1, Player parent2) {
        //copy the genes but do not modify the parent ones
        List<Float> genes1 = new ArrayList<>(parent1.exposeNeuronNetwork().getWeights());
        List<Float> genes2 = new ArrayList<>(parent2.exposeNeuronNetwork().getWeights());
        chromosomalCrossover(genes1, genes2);
        players.add(new Player(pApplet, genes1));
        players.add(new Player(pApplet, genes2));
    }

    private void mutateBabies(List<Integer> listOfAlreadyMutated) {
        int randomIndex = getRandomIndex(players.size() - 1);
        while (listOfAlreadyMutated.contains(randomIndex)) {
            randomIndex = getRandomIndex(players.size() - 1);
        }
        listOfAlreadyMutated.add(randomIndex);
        List<Float> genes = players.get(randomIndex).exposeNeuronNetwork().getWeights();
        for (int i = 0; i < genes.size(); i++) {
            if (maybeYes()) {
                genes.set(i, genes.get(i) * generateRandomWeight());
            }
        }
    }

    private void chromosomalCrossover(List<Float> parent1Genes, List<Float> parent2Genes) {
        for (int i = 0; i < parent1Genes.size(); i++) {
            //swap random genes
            if (maybeYes()) {
                float temp = parent1Genes.get(i);
                parent1Genes.set(i, parent2Genes.get(i));
                parent2Genes.set(i, temp);
            }
        }
    }

    private List<Float> generateRandomWeights() {
        List<Float> result = new ArrayList<>();
        int weightsAmount = Configuration.AMOUNT_OF_INPUT_NEURONS * Configuration.AMOUNT_OF_HIDDEN_NEURONS;
        weightsAmount += Configuration.AMOUNT_OF_HIDDEN_NEURONS * Configuration.AMOUNT_OF_OUTPUT_NEURONS;
        for (int i = 0; i < weightsAmount; i++) {
            result.add(generateRandomWeight());
        }
        return result;
    }

    private float generateRandomWeight() {
        return pApplet.random(-1, 1);
    }

    private int getRandomIndex(int maxValue) {
        return (int) pApplet.random(maxValue);
    }

    private boolean maybeYes() {
        return (int) pApplet.random(5) > 2;
    }
}
