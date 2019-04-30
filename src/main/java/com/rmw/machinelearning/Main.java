package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.ArrayList;
import java.util.List;

import static com.rmw.machinelearning.Configuration.AMOUNT_OF_PLAYERS;
import static com.rmw.machinelearning.Configuration.HEIGHT;
import static com.rmw.machinelearning.Configuration.IS_AI_MODE;
import static com.rmw.machinelearning.Configuration.WIDTH;

public class Main extends PApplet {

    private final List<ScreenObject> obstacles = new ArrayList<>();
    private final GameScreenGraph gameScreenGraph = new GameScreenGraph(obstacles);
    private Population population;
    private Player player;
    private Monster monster;

    public static void main(final String[] args) {
        PApplet.main("com.rmw.machinelearning.Main", args);
    }

    @Override
    public void settings() {
        size(WIDTH, HEIGHT);
    }

    @Override
    public void setup() {
        initializeObstacles();
        gameScreenGraph.setUpGraph();
        if (IS_AI_MODE) {
            if (AMOUNT_OF_PLAYERS < 4) {
                throw new IllegalArgumentException("Amount of players can't be less than 4");
            }
            population = new Population(this, obstacles);
        } else {
            player = new Player(this, obstacles);
            monster = new Monster(this, gameScreenGraph);
            monster.setPosition(40, 40);
            monster.setTarget(player);
            monster.setColour(255, 0, 0);
            obstacles.add(monster);
        }
    }

    @Override
    public void draw() {
        background(0, 0, 255);
        obstacles.forEach(ScreenObject::update);
        if (IS_AI_MODE) {
            population.update();
        } else {
            player.update();
            monster.update();
        }
    }

    @Override
    public void keyPressed() {
        switch (keyCode) {
            case LEFT:
                player.setVelocity(-2, 0);
                break;
            case RIGHT:
                player.setVelocity(2, 0);
                break;
            case UP:
                player.setVelocity(0, -2);
                break;
            case DOWN:
                player.setVelocity(0, 2);
                break;
            default:
                player.setVelocity(0, 0);
                player.stop();
        }
    }

    private void initializeObstacles() {
        //add screen borders
        obstacles.add(new Wall(this, 0, 0, WIDTH, 1));
        obstacles.add(new Wall(this, 0, 0, 1, HEIGHT));
        obstacles.add(new Wall(this, WIDTH, 0, 1, HEIGHT));
        obstacles.add(new Wall(this, 0, HEIGHT, WIDTH, 1));
        //setup walls
        obstacles.add(new Wall(this, 0, 0, 10, HEIGHT));
        obstacles.add(new Wall(this, 0, 0, WIDTH, 10));
        obstacles.add(new Wall(this, WIDTH - 10, 0, 10, HEIGHT));
        obstacles.add(new Wall(this, 0, HEIGHT - 10, WIDTH, 10));
        obstacles.add(new Wall(this, 100, 50, 200, 50));
        obstacles.add(new Wall(this, 100, 100, 50, 50));
        obstacles.add(new Wall(this, 200, 250, 50, 300));
        obstacles.add(new Wall(this, 250, 650, 50, 150));
        obstacles.add(new Wall(this, 300, 200, 250, 50));
        obstacles.add(new Wall(this, 300, 250, 50, 50));
        obstacles.add(new Wall(this, 350, 700, 50, 100));
        obstacles.add(new Wall(this, 400, 450, 50, 200));
        obstacles.add(new Wall(this, 500, 50, 250, 50));
        obstacles.add(new Wall(this, 500, 300, 50, 100));
        obstacles.add(new Wall(this, 500, 450, 150, 50));
        obstacles.add(new Wall(this, 500, 500, 50, 100));
        obstacles.add(new Wall(this, 500, 650, 50, 150));
        obstacles.add(new Wall(this, 600, 350, 50, 150));
        obstacles.add(new Wall(this, 650, 650, 100, 50));
        obstacles.add(new Wall(this, 700, 100, 50, 500));
    }
}

