package com.rmw.machinelearning;

/**
 * Indicates the direction and distance to another object
 * on one of the 4 possible directions
 */
class Side {

    private final Direction direction;
    private final float distance;

    Side(final Direction direction, final float distance) {
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
