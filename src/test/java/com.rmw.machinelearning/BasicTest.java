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

        final Vision vision = new Vision();
        vision.put(Direction.TOP, 1);
        vision.put(Direction.BOTTOM, 1);
        vision.put(Direction.LEFT, 1);
        vision.put(Direction.RIGHT, 1);
        final NeuralNetwork neuralNetwork = new NeuralNetwork(weights);
        neuralNetwork.setInputs(vision);
        final PVector pVector = neuralNetwork.react();
        System.out.println("x: " + pVector.x + " y: " + pVector.y);
    }

}


