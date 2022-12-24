package com.coehlrich.adventofcode.day24;

import com.coehlrich.adventofcode.util.Direction;
import com.coehlrich.adventofcode.util.Point2;

public record Blizzard(Point2 point, Direction direction) {
    public Blizzard move(int maxY, int maxX) {
        Point2 newP = direction.offset(point);
        newP = new Point2(((newP.x() % maxX) + maxX) % maxX, ((newP.y() % maxY) + maxY) % maxY);
        return new Blizzard(newP, direction);
    }
}
