package com.rmw.machinelearning;

public enum InputNeuron {

    RIGHT("I1"),
    LEFT("I2"),
    TOP("I3"),
    BOTTOM("I4"),
    BIAS("I5");

    private String inputNeuron;

    public String getCode() {
        return inputNeuron;
    }

    InputNeuron(String inputNeuron) {
        this.inputNeuron = inputNeuron;
    }

}
