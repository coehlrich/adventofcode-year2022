package com.coehlrich.adventofcode.day22;

public enum Side {
    DOWN,
    UP,
    FORWARD,
    LEFT,
    BACK,
    RIGHT;

    public Side get(Direction dir) {
        return switch (this) {
            case DOWN -> {
                yield switch (dir) {
                    case DOWN -> BACK;
                    case LEFT -> LEFT;
                    case RIGHT -> RIGHT;
                    case UP -> FORWARD;
                };
            }
            case BACK -> {
                yield switch (dir) {
                    case UP -> UP;
                    case LEFT -> RIGHT;
                    case RIGHT -> LEFT;
                    case DOWN -> DOWN;
                };
            }
            case FORWARD -> {
                yield switch (dir) {
                    case DOWN -> DOWN;
                    case LEFT -> LEFT;
                    case RIGHT -> RIGHT;
                    case UP -> UP;
                };
            }
            case LEFT -> {
                yield switch (dir) {
                    case DOWN -> FORWARD;
                    case LEFT -> DOWN;
                    case RIGHT -> UP;
                    case UP -> BACK;
                };
            }
            case RIGHT -> {
                yield switch (dir) {
                    case DOWN -> BACK;
                    case LEFT -> DOWN;
                    case RIGHT -> UP;
                    case UP -> FORWARD;
                };
            }
            case UP -> {
                yield switch (dir) {
                    case DOWN -> FORWARD;
                    case LEFT -> LEFT;
                    case RIGHT -> RIGHT;
                    case UP -> BACK;
                };
            }
        };
    }
}
