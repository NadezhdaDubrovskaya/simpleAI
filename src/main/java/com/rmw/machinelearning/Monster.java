package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

import static com.rmw.machinelearning.Configuration.MONSTER_RADIUS;
import static java.util.Collections.emptyList;

class Monster extends Player {

    private final GameScreenGraph gameScreenGraph;
    private Player target;

    Monster(final PApplet p, final GameScreenGraph gameScreenGraph) {
        super(p, emptyList()); //monsters do not care about the obstacles
        this.gameScreenGraph = gameScreenGraph;
        setRadius(MONSTER_RADIUS);
    }

    void setTarget(final Player target) {
        this.target = target;
    }

    @Override
    void move() {
        final List<PVector> path = gameScreenGraph.calculatePath(getPosition(), target.getPosition());
        if (path != null && !path.isEmpty()) {
            final PVector nextPosition = selectNextPosition(path);
            final float difX = getPosition().x - nextPosition.x;
            final float difY = getPosition().y - nextPosition.y;
            setVelocity(0, 0);
            if (difX != 0 && difX < 0) {
                setVelocity(1, getVelocity().y);
            } else if (difX > 0) {
                setVelocity(-1, getVelocity().y);
            }
            if (difY != 0 && difY < 0) {
                setVelocity(getVelocity().x, 1);
            } else if (difY > 0) {
                setVelocity(getVelocity().x, -1);
            }
            getPosition().add(getVelocity());
        }
    }

    private PVector selectNextPosition(final List<PVector> path) {
        int i = 1;
        while (i < path.size()) {
            final PVector nextPosition = path.get(i);
            if (nextPosition.equals(getPosition())) {
                i++;
            } else {
                return nextPosition;
            }
        }
        return getPosition();
    }
}
