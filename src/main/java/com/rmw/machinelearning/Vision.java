package com.rmw.machinelearning;

import java.util.HashMap;
import java.util.Map;

/**
 * Contains information about the presence of obstacles near the player for neural network.
 * If value is 0 then nothing is near the player on that side thus the neural network should act correspondingly.
 * Values on each
 */
class Vision {

    private final Map<Direction, Float> sides = new HashMap<>();

    Vision() {
        reset();
    }

    void put(final Direction direction, final float value) {
        sides.put(direction, value);
    }

    float get(final Direction direction) {
        return sides.get(direction);
    }

    Map<Direction, Float> getSides() {
        return sides;
    }

    void reset() {
        for (final Direction direction : Direction.values()) {
            sides.put(direction, 0f);
        }
        sides.put(Direction.BIAS, 1f); //bias neuron has always 1 as value
    }
}
