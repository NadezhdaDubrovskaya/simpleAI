package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class GeneticAlgorithm {

    private PApplet pApplet;
    private static final int AMOUNT_OF_PLAYERS = 500;
    private List<Player> players;

    GeneticAlgorithm(PApplet p) {
        pApplet = p;
    }

    List<Player> getInitialPopulation() {
        players = new ArrayList<>();
        for (int i = 0; i < AMOUNT_OF_PLAYERS; i++) {
            players.add(new Player(pApplet, generateRandomWeights()));
        }
        return players;
    }

    List<Player> getNextPopulation() {
        int topBestPlayersThreshold = 5;
        double percentageOfCrossoverBabies = 0.6;
        double percentageOfMutatedBabies = 0.4;

        sortPlayerBasedOnFitnessFunction();
        List<Player> bestPlayers = new ArrayList<>(players.subList(0, topBestPlayersThreshold));
        List<Float> bestGenes = bestPlayers.get(0).exposeNeuronNetwork().getWeights();
        PApplet.println("Best player has fitness score of " + bestPlayers.get(0).getFitnessScore());
        PApplet.println("Best genes: " + bestGenes);
        players.clear();

        for (int i = 0; i < (AMOUNT_OF_PLAYERS * percentageOfCrossoverBabies) / 2; i++) {
            breedRegularBabies(bestPlayers.get(0), bestPlayers.get((int) pApplet.random(1, topBestPlayersThreshold - 1)));
        }
        for (int i = 0; i < (AMOUNT_OF_PLAYERS * percentageOfMutatedBabies) / 2; i++) {
            breedMutatedBabies(bestPlayers.get(0), bestPlayers.get((int) pApplet.random(1, topBestPlayersThreshold - 1)));
        }
        players.add(new Player(pApplet, bestGenes));

        return players;
    }

    private void sortPlayerBasedOnFitnessFunction() {
        players.forEach(Player::calculateFitness);
        players.sort(Comparator.comparing(Player::getFitnessScore).reversed());
    }

    private void breedRegularBabies(Player parent1, Player parent2) {
        //copy the genes but do not modify the parent ones
        List<Float> genes1 = new ArrayList<>(parent1.exposeNeuronNetwork().getWeights());
        List<Float> genes2 = new ArrayList<>(parent2.exposeNeuronNetwork().getWeights());
        chromosomalCrossover(genes1, genes2);
        players.add(new Player(pApplet, genes1));
        players.add(new Player(pApplet, genes2));
    }

    private void breedMutatedBabies(Player parent1, Player parent2) {
        //copy the genes but do not modify the parent ones
        List<Float> genes1 = new ArrayList<>(parent1.exposeNeuronNetwork().getWeights());
        List<Float> genes2 = new ArrayList<>(parent2.exposeNeuronNetwork().getWeights());
        chromosomalCrossover(genes1, genes2);
        for (int i = 0; i < genes1.size(); i++) {
            if ((int) pApplet.random(5) > 2) {
                genes1.set(i, generateRandomWeight());
            }
            if ((int) pApplet.random(5) > 2) {
                genes2.set(i, generateRandomWeight());
            }
        }
        players.add(new Player(pApplet, genes1));
        players.add(new Player(pApplet, genes2));
    }

    private void chromosomalCrossover(List<Float> parent1Genes, List<Float> parent2Genes) {
        for (int i = 0; i < parent1Genes.size(); i++) {
            //swap random genes
            if ((int) pApplet.random(5) > 2) {
                float temp = parent1Genes.get(i);
                parent1Genes.set(i, parent2Genes.get(i));
                parent2Genes.set(i, temp);
            }
        }
    }

    private List<Float> generateRandomWeights() {
        List<Float> result = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            result.add(generateRandomWeight());
        }
        return result;
    }

    private float generateRandomWeight() {
        return pApplet.random(-1, 1);
    }
}
