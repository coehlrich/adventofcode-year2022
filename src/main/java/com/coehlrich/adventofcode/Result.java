package com.coehlrich.adventofcode;

public record Result(String part1, String part2) {
    public Result(int part1, int part2) {
        this(Integer.toString(part1), Integer.toString(part2));
    }
}
