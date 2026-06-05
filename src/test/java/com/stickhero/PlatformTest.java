package com.stickhero;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PlatformTest {
    @Test
    void rightEdgeCalculator() {
        Platform p = new Platform(100, 50, 120);
        assertEquals(150,p.getRightEdge());
    }

    @Test
    void leftEdgeCalculator(){
        Platform p = new Platform(100, 50, 120);
        assertEquals(100,p.getLeftEdge());
    }
}
