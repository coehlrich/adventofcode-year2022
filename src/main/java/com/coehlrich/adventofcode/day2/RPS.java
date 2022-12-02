package com.coehlrich.adventofcode.day2;

public enum RPS {
    ROCK,
    PAPER,
    SCISSORS;

    public int score() {
        return switch (this) {
            case ROCK -> 1;
            case PAPER -> 2;
            case SCISSORS -> 3;
        };
    }

    public RPS beats() {
        return switch (this) {
            case ROCK -> SCISSORS;
            case PAPER -> ROCK;
            case SCISSORS -> PAPER;
        };
    }
}
