package dev.rndmorris.salisarcana.lib;

import java.util.Random;
import java.util.stream.IntStream;

public class RandomHelper {

    public static int weightedRandom(Random random, int[] weights) {
        int fullWeight = IntStream.of(weights)
            .sum();
        if (fullWeight <= 0) {
            return -1;
        }
        int r = random.nextInt(fullWeight);
        int cumulativeWeight = 0;

        for (int i = 0; i < weights.length; i++) {
            cumulativeWeight += weights[i];
            if (r < cumulativeWeight) { // Check against cumulative weight
                return i;
            }
        }
        return -1;
    }
}
