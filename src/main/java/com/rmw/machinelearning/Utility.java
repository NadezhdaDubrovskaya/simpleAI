package com.rmw.machinelearning;

import java.util.Random;

class Utility {
    private final static Random rand = new Random();

    static boolean maybeYes() {
        return rand.nextBoolean();
    }

}

//return (int) rand.nextInt(5 + 1 - 2) + 2;