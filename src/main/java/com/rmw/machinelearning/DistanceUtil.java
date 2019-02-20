package com.rmw.machinelearning;

class DistanceUtil {

    /**
     * Calculates the distance between object 1 and 2, where first is the referenced one
     * (meaning that it is considered as the main object from which we want to get the direction
     * and the distance to another object)
     * <p>
     * If passed objects do not lie on any of the 4 possible directions, null is going to be returned
     * In case of the collision the distance is going to be 0
     */
    static Distance calculateDistance(final ScreenObject object1, final ScreenObject object2) {

        final boolean sameVertical =
                object1.getRightBorder() >= object2.getLeftBorder()
                        && object1.getLeftBorder() <= object2.getRightBorder();
        final boolean sameHorizontal =
                object1.getTopBorder() <= object2.getBottomBorder()
                        && object1.getBottomBorder() >= object2.getTopBorder();

        if (sameHorizontal && sameVertical) {
            // means that the objects are colliding
            return new Distance(Direction.BIAS, 0);
        }

        if (sameVertical) {
            final float distanceOnY = object1.getTopBorder() - object2.getBottomBorder();
            if (distanceOnY > 0) {
                return new Distance(Direction.TOP, distanceOnY);
            } else {
                return new Distance(Direction.BOTTOM, object2.getTopBorder() - object1.getBottomBorder());
            }
        }
        if (sameHorizontal) {
            final float distanceOnX = object1.getLeftBorder() - object2.getRightBorder();
            if (distanceOnX > 0) {
                return new Distance(Direction.LEFT, distanceOnX);
            } else {
                return new Distance(Direction.RIGHT, object2.getLeftBorder() - object1.getRightBorder());
            }
        }
        return null;
    }
}
