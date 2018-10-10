package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

class GeneticAlgorithm {

    private PApplet pApplet;
    private static final int AMOUNT_OF_PLAYERS = 100;
    private static final float MUTATION_RATIO = 0.001f;
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
        sortPlayerBasedOnFitnessFunction();
        Player best = players.get(0);
        Player secondBest = players.get(1);
        PApplet.println("Best player survived for " + best.getLifespan());
        PApplet.println("Second best player survived for " + secondBest.getLifespan());
        PApplet.println("Parent 1 genes: " + best.exposeNeuronNetwork().getWeights());
        PApplet.println("Parent 2 genes: " + secondBest.exposeNeuronNetwork().getWeights());
        players.clear();
        for (int i = 0; i < AMOUNT_OF_PLAYERS/2; i++) {
            crossoverGenes(best, secondBest);
        }
        return players;
    }

    private void sortPlayerBasedOnFitnessFunction() {
        players.sort(Comparator.comparing(Player::getLifespan).reversed());
    }

    private void crossoverGenes(Player parent1, Player parent2) {
        //copy the genes but do not modify the parent ones
        List<Float> genes1 = new ArrayList<>(parent1.exposeNeuronNetwork().getWeights());
        List<Float> genes2 = new ArrayList<>(parent2.exposeNeuronNetwork().getWeights());
        int randomIndex = (int) pApplet.random(0, genes2.size());
        for (int i = 0; i < randomIndex; i++) {
            float temp = genes1.get(i);
            genes1.set(i, genes2.get(i));
            genes2.set(i, temp);
        }
        players.add(new Player(pApplet, genes1));
        players.add(new Player(pApplet, genes2));
        PApplet.println("New babies are ready!");
        PApplet.println("Baby 1 genes: "+ genes1);
        PApplet.println("Baby 2 genes: "+ genes2);
    }

    private List<Float> generateRandomWeights() {
        List<Float> result = new ArrayList<>();
        for (int i = 0; i < 36; i++) {
            result.add((pApplet.random(-2, 2)));
        }
        return result;
    }

    private void mutate(NeuronNetwork neuronNetwork) {
        List<Float> weights = neuronNetwork.getWeights();
        int randomIndex = (int) pApplet.random(weights.size());
        weights.set(randomIndex, weights.get(randomIndex) * MUTATION_RATIO);
    }


}
