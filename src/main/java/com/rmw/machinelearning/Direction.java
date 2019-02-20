package com.rmw.machinelearning;

//TODO for input neurons make another enum. This is going to be only direction enum
public enum Direction {

    RIGHT("I1"),
    LEFT("I2"),
    TOP("I3"),
    BOTTOM("I4"),
    BIAS("I5");

    private final String inputNeuron;

    Direction(final String inputNeuron) {
        this.inputNeuron = inputNeuron;
    }

    public String getCode() {
        return inputNeuron;
    }

}
