package com.coehlrich.adventofcode.day9;

public record Movement(char direction, int distance) {
    public static Movement parse(String value) {
        String[] split = value.split(" ");
        return new Movement(split[0].charAt(0), Integer.parseInt(split[1]));
    }
}
