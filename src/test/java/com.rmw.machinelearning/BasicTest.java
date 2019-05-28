package com.rmw.machinelearning;

import org.junit.Test;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class BasicTest {

    @Test
    public void testNeuralNetworkCalculation() {
        final List<Float> weights = new ArrayList<>();
        weights.add(1f); weights.add(1f);
        weights.add(1f); weights.add(1f);
        weights.add(1f); weights.add(1f);
        weights.add(1f); weights.add(1f);
        weights.add(1f); weights.add(1f);
        weights.add(1f); weights.add(1f);
        weights.add(1f); weights.add(1f);
        weights.add(1f); weights.add(1f);
        weights.add(1f); weights.add(1f);
        weights.add(1f); weights.add(1f);
        weights.add(1f); weights.add(1f);
        weights.add(1f);

        final NeuralNetwork neuralNetwork = new NeuralNetwork(weights);
        final PVector pVector = neuralNetwork.react();
        System.out.println("x: " + pVector.x + " y: " + pVector.y);
    }

}


