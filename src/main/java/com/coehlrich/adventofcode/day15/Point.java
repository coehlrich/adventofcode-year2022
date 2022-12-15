package com.coehlrich.adventofcode.day15;

public record Point(int x, int y) {
    public int manhatten(Point o) {
        return Math.abs(x - o.x) + Math.abs(y - o.y);
    }
}
