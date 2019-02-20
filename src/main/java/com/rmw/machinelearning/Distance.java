package com.rmw.machinelearning;

/**
 * Indicates the direction and distance to another object
 */
class Distance {

    private final Direction direction;
    private final float distance;

    Distance(final Direction direction, final float distance) {
        this.direction = direction;
        this.distance = distance;
    }

    Direction getDirection() {
        return direction;
    }

    float getDistance() {
        return distance;
    }
}
