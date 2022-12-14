package com.coehlrich.adventofcode.day14;

import java.util.List;
import java.util.stream.IntStream;

public record Point(int x, int y) {
    public List<Point> getPointsTo(Point o) {
        if (x != o.x) {
            return IntStream.rangeClosed(Math.min(x, o.x), Math.max(x, o.x))
                    .mapToObj((integer) -> new Point(integer, y)).toList();
        } else {
            return IntStream.rangeClosed(Math.min(y, o.y), Math.max(y, o.y))
                    .mapToObj((integer) -> new Point(x, integer)).toList();
        }
    }
}
