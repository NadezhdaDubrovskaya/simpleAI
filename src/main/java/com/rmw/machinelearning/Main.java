package com.rmw.machinelearning;

import processing.core.PApplet;

import java.util.HashSet;
import java.util.Set;

import static com.rmw.machinelearning.Configuration.AMOUNT_OF_PLAYERS;
import static com.rmw.machinelearning.Configuration.HEIGHT;
import static com.rmw.machinelearning.Configuration.IS_AI_MODE;
import static com.rmw.machinelearning.Configuration.WIDTH;

public class Main extends PApplet {

    private final Obstacles obstacles = Obstacles.getInstance(this);
    private final GameScreenGraph gameScreenGraph = new GameScreenGraph();
    private final Set<CircularObject> circularObjects = new HashSet<>();
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
        if (IS_AI_MODE) {
            if (AMOUNT_OF_PLAYERS < 4) {
                throw new IllegalArgumentException("Amount of players can't be less than 4");
            }
            population = new Population(this);
        } else {
            player = new Player(this);
            monster = new Monster(this, gameScreenGraph);
            monster.setPosition(40, 40);
            monster.setTarget(player);
            monster.setColour(255, 0, 0);
            obstacles.addEvilAI(monster);
        }
/*        gameScreenGraph.getAllVertexes().forEach(
                vertex -> {
                    final Player vertexPlayer = new Player(this);
                    vertexPlayer.setRadius(5);
                    vertexPlayer.setPosition(vertex.x, vertex.y);
                    circularObjects.add(vertexPlayer);
                }
        );*/
    }

    @Override
    public void draw() {
        background(0, 0, 255);
        obstacles.update();
        if (IS_AI_MODE) {
            population.update();
        } else {
            player.update();
            monster.update();
        }
        // circularObjects.forEach(CircularObject::update);
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
}

