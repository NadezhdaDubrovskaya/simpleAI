package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.LinkedList;
import java.util.List;

import static com.rmw.machinelearning.Configuration.AMOUNT_OF_PLAYERS;
import static com.rmw.machinelearning.Configuration.HEIGHT;
import static com.rmw.machinelearning.Configuration.WIDTH;
import static com.rmw.machinelearning.State.DONE;
import static com.rmw.machinelearning.State.IDLE;

public class Main extends PApplet {

    private final Obstacles obstacles = Obstacles.getInstance(this);
    private final List<MovingWall> movingWallList = new LinkedList<>();
    private final Population population = new Population(this);

    public static void main(final String[] args) {
        PApplet.main("com.rmw.machinelearning.Main", args);
    }

    public void settings() {
        size(WIDTH, HEIGHT);
    }

    public void setup() {
        if (AMOUNT_OF_PLAYERS < 4) {
            throw new IllegalArgumentException("Amount of players can't be less than 4");
        }
        movingWallList.add(new MovingWall(this, 0, 0, Direction.RIGHT, 10, HEIGHT));
        movingWallList.add(new MovingWall(this, WIDTH - 10, 0, Direction.LEFT, 10, HEIGHT));
        movingWallList.add(new MovingWall(this, 0, 0, Direction.BOTTOM, WIDTH, 10));
        movingWallList.add(new MovingWall(this, 0, HEIGHT - 10, Direction.TOP, WIDTH, 10));
        obstacles.getObstacles().addAll(movingWallList);
    }

    public void draw() {
        background(0, 0, 255);
        manageMovingWalls();
        obstacles.update();
        population.update();
    }

    private void manageMovingWalls() {
        if (movingWallList.stream().allMatch(movingWall -> movingWall.getState() == IDLE)) {
            movingWallList.get(0).start();
        }
        for (int i = 0; i < movingWallList.size(); i++) {
            final MovingWall movingWall = movingWallList.get(i);
            if (movingWall.getState() == DONE) {
                movingWall.reset();
                final int indexOfNextWall = (i != movingWallList.size() - 1) ? i + 1 : 0;
                PApplet.println("Starting the wall under index " + indexOfNextWall);
                movingWallList.get(indexOfNextWall).start();
                break;
            }
        }
    }
}

