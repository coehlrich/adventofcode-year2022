package com.coehlrich.adventofcode.util;

public enum Direction {
    UP(0, -1),
    RIGHT(1, 0),
    DOWN(0, 1),
    LEFT(-1, 0);

    public final int x;
    public final int y;

    private Direction(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Point2 offset(Point2 point) {
        return new Point2(point.x() + x, point.y() + y);
    }
}
