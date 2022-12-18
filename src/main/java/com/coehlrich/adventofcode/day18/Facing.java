package com.coehlrich.adventofcode.day18;

public enum Facing {
    UP(0, 1, 0),
    DOWN(0, -1, 0),
    NORTH(0, 0, 1),
    SOUTH(0, 0, -1),
    WEST(-1, 0, 0),
    EAST(1, 0, 0);

    public final int x;
    public final int y;
    public final int z;

    private Facing(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Position offset(Position pos) {
        return new Position(pos.x() + x, pos.y() + y, pos.z() + z);
    }
}
