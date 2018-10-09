package com.rmw.machinelearning;

public enum Direction {

    RIGHT("I1"),
    LEFT("I2"),
    TOP("I3"),
    BOTTOM("I4");

    private String inputNeuron;

    public String getInputNeuron() {
        return inputNeuron;
    }

    Direction(String inputNeuron) {
        this.inputNeuron = inputNeuron;
    }

}
