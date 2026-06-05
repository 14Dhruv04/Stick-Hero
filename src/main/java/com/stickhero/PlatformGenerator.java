package com.stickhero;

import java.util.Random;

public class PlatformGenerator {
    private static final double MIN_GAP = 60, MAX_GAP = 470;
    private static final double MIN_WIDTH = 40, MAX_WIDTH = 180;
    private Random random = new Random();

    public Platform generateNext(Platform current) {
        double gap = MIN_GAP + random.nextDouble() * (MAX_GAP - MIN_GAP);
        double w   = MIN_WIDTH + random.nextDouble() * (MAX_WIDTH - MIN_WIDTH);
        double x   = current.getRightEdge() + gap;
        return new Platform(x, w, current.getHeight());
    }
}
