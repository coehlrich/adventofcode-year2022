package com.coehlrich.adventofcode.day18;

import java.util.stream.Stream;

public record Position(int x, int y, int z) {
    public static Position parse(String line) {
        int[] coordinates = Stream.of(line.split(",")).mapToInt(Integer::parseInt).toArray();

        return new Position(coordinates[0], coordinates[1], coordinates[2]);
    }
}
