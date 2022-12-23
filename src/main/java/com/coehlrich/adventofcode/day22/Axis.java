package com.coehlrich.adventofcode.day22;

import java.util.List;

public enum Axis {
    X(List.of(Direction.LEFT, Direction.RIGHT)),
    Z(List.of(Direction.UP, Direction.DOWN));

    public final List<Direction> directions;

    private Axis(List<Direction> directions) {
        this.directions = directions;
    }

    public static Axis get(Direction direction) {
        return switch (direction) {
            case LEFT, RIGHT -> X;
            case UP, DOWN -> Z;
        };
    }
}
