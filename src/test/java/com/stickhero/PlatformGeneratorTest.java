package com.stickhero;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class PlatformGeneratorTest {
    @Test
    void nextPlatformAppearsToRight() {
        PlatformGenerator generator = new PlatformGenerator();
        Platform current = new Platform(0,100,120);

        Platform next = generator.generateNext(current);

        assertTrue(next.getLeftEdge() > current.getRightEdge());
    }
}
