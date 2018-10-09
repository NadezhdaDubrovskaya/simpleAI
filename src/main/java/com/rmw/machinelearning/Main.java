package com.rmw.machinelearning;

import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

public class Main extends PApplet{

    private static final int AMOUNT_OF_PLAYERS = 2;

    public static void main(String[] args) {
        PApplet.main("com.rmw.machinelearning.Main", args);
    }

    private List<Player> players;
    private List<Wall> walls = Obstacles.getInstance().getObstacles();
    private int counter = 1000; //how long to let players live

    public void settings() {
        size(800, 800);
    }

    public void setup() {
        players = new ArrayList<>();
        walls.add(new Wall(this, 600,100,50,200));
        walls.add(new Wall(this,100,100,50,200));
        walls.add(new Wall(this, 200,500,50,200));

        for (int i = 0; i < AMOUNT_OF_PLAYERS; i ++) {
            players.add(new Player(this, generateRandomWeights()));
        }
    }

    public void draw() {
        background(0,0,255);
        for (Wall wall: walls) {
            wall.show();
        }
        for (Player player: players) {
            player.look();
            player.think();
            player.move();
            player.show();
            player.checkForCollisions();
        }
        /*
        if (counter > 0) {
            counter--;
        } else {
            findBestPlayers();
            destroyCurrentGeneration();
            getNewGeneration();
        }
        */
    }

    public void keyPressed()
    {

            if (keyCode == LEFT)
            {
                players.get(0).setAcceleration(new PVector(-5, 0));
            }
            if(keyCode == RIGHT)
            {
                players.get(0).setAcceleration(new PVector(5, 0));
            }
            if (keyCode == UP)
            {
                players.get(0).setAcceleration(new PVector(0, -5));
            }
            if(keyCode == DOWN)
            {
                players.get(0).setAcceleration(new PVector(0, 5));
            }

    }


    private void findBestPlayers() {
        List<Player> champions = new ArrayList<>();
        players.forEach(player -> {
            if (!player.isDead()) {
                champions.add(player);
            }
        });
        println(champions.size() + " are still alive!");
        println("Here are their weights");
        for (Player champion: champions) {
            // println(champion.brain.weights);
        }
    }

    private void destroyCurrentGeneration() {


    }

    private void getNewGeneration() {

    }

    private List<Float> generateRandomWeights() {
        List<Float> result = new ArrayList<>();
        for (int i = 0; i< 41; i++) {
            result.add(random(-2,2));
        }
        return result;
    }


}
