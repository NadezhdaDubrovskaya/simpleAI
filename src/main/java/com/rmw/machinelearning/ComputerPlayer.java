package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.List;

class ComputerPlayer extends Player {

    private Brain brain;
    private int survivedForXMoves;
    private int fitnessScore;

    ComputerPlayer(final PApplet p, final List<ScreenObject> obstacles) {
        super(p, obstacles);
        reset();
    }

    @Override
    void reset() {
        super.reset();
        survivedForXMoves = 0;
        fitnessScore = 0;
    }

    @Override
    void update() {
        if (!isDead()) {
            brain.look();
            brain.react();
        }
        super.update();
    }

    @Override
    void move() {
        super.move();
        if (!isDead()) {
            survivedForXMoves++;
        }
    }

    int getFitnessScore() {
        return fitnessScore;
    }

    void calculateFitness() {
        fitnessScore = survivedForXMoves;
    }

    List<Float> getWeights() {
        return brain.getWeights();
    }

    Brain getBrain() {
        return brain;
    }

    void setBrain(final Brain brain) {
        this.brain = brain;
    }

}
