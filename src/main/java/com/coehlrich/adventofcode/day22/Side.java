package com.coehlrich.adventofcode.day22;

public enum Side {
    DOWN,
    UP,
    FORWARD,
    LEFT,
    BACK,
    RIGHT;

//    public Side get(Direction dir) {
//        return switch (this) {
//            case DOWN -> {
//                yield switch (dir) {
//                    case DOWN -> BACK;
//                    case LEFT -> LEFT;
//                    case RIGHT -> RIGHT;
//                    case UP -> FORWARD;
//                };
//            }
//            case BACK -> {
//                yield switch (dir) {
//                    case UP -> UP;
//                    case LEFT -> RIGHT;
//                    case RIGHT -> LEFT;
//                    case DOWN -> DOWN;
//                };
//            }
//            case FORWARD -> {
//                yield switch (dir) {
//                    case DOWN -> DOWN;
//                    case LEFT -> LEFT;
//                    case RIGHT -> RIGHT;
//                    case UP -> UP;
//                };
//            }
//            case LEFT -> {
//                yield switch (dir) {
//                    case DOWN -> FORWARD;
//                    case LEFT -> DOWN;
//                    case RIGHT -> UP;
//                    case UP -> BACK;
//                };
//            }
//            case RIGHT -> {
//                yield switch (dir) {
//                    case DOWN -> BACK;
//                    case LEFT -> DOWN;
//                    case RIGHT -> UP;
//                    case UP -> FORWARD;
//                };
//            }
//            case UP -> {
//                yield switch (dir) {
//                    case DOWN -> FORWARD;
//                    case LEFT -> LEFT;
//                    case RIGHT -> RIGHT;
//                    case UP -> BACK;
//                };
//            }
//        };
//    }

    public Side rotateX(int amount) {
        Side side = this;
        if (amount > 0) {
            for (int i = 0; i < amount; i++) {
                side = switch (side) {
                    case BACK -> UP;
                    case DOWN -> BACK;
                    case UP -> FORWARD;
                    case FORWARD -> DOWN;
                    default -> side;
                };
            }
        } else if (amount < 0) {
            for (int i = 0; i < -amount; i++) {
                side = switch (side) {
                    case BACK -> DOWN;
                    case DOWN -> FORWARD;
                    case UP -> BACK;
                    case FORWARD -> UP;
                    default -> side;
                };
            }
        }
        return side;
    }

    public Side rotateY(int amount) {
        Side side = this;
        if (amount > 0) {
            for (int i = 0; i < amount; i++) {
                side = switch (side) {
                    case BACK -> LEFT;
                    case LEFT -> FORWARD;
                    case FORWARD -> RIGHT;
                    case RIGHT -> BACK;
                    default -> side;
                };
            }
        } else if (amount < 0) {
            for (int i = 0; i < -amount; i++) {
                side = switch (side) {
                    case BACK -> RIGHT;
                    case LEFT -> BACK;
                    case FORWARD -> LEFT;
                    case RIGHT -> FORWARD;
                    default -> side;
                };
            }
        }
        return side;
    }

    public Side rotateZ(int amount) {
        Side side = this;
        if (amount > 0) {
            for (int i = 0; i < amount; i++) {
                side = switch (side) {
                    case RIGHT -> DOWN;
                    case DOWN -> LEFT;
                    case UP -> RIGHT;
                    case LEFT -> UP;
                    default -> side;
                };
            }
        } else if (amount < 0) {
            for (int i = 0; i < -amount; i++) {
                side = switch (side) {
                    case RIGHT -> UP;
                    case DOWN -> RIGHT;
                    case UP -> LEFT;
                    case LEFT -> DOWN;
                    default -> side;
                };
            }
        }
        return side;
    }

    public boolean onHorizontalDirection() {
        return switch (this) {
            case LEFT, BACK, RIGHT, FORWARD -> true;
            case UP, DOWN -> false;
        };
    }

    public Side opposite() {
        return switch (this) {
            case DOWN -> UP;
            case UP -> DOWN;
            case LEFT -> RIGHT;
            case RIGHT -> LEFT;
            case FORWARD -> BACK;
            case BACK -> FORWARD;
        };
    }
}
