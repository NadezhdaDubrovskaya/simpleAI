package com.rmw.machinelearning;

public enum Direction {

    RIGHT(0),
    LEFT(1),
    TOP(2),
    BOTTOM(3),
    BIAS(4);

    private final int neuronIndex;

    Direction(final int neuronIndex) {
        this.neuronIndex = neuronIndex;
    }

    public int getIndex() {
        return neuronIndex;
    }

}
