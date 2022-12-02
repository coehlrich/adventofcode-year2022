package com.coehlrich.adventofcode.day2;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

public class Main implements Day {

    @Override
    public int day() {
        return 2;
    }

    @Override
    public Result execute(String input) {

        return new Result(calculate(input, false), calculate(input, true));
    }

    public int calculate(String input, boolean part2) {
        return input.lines().mapToInt((line) -> {
            RPS opponent = switch (line.charAt(0)) {
                case 'A' -> RPS.ROCK;
                case 'B' -> RPS.PAPER;
                case 'C' -> RPS.SCISSORS;
                default -> throw new IllegalStateException();
            };

            RPS you = switch (line.charAt(2)) {
                case 'X' -> RPS.ROCK;
                case 'Y' -> RPS.PAPER;
                case 'Z' -> RPS.SCISSORS;
                default -> throw new IllegalStateException();
            };

            int outcome = 0;
            if (you == opponent) {
                outcome = 3;
            } else if (you.beats() == opponent) {
                outcome = 6;
            }

            int part2S = opponent.score() + you.score() - 2;
            while (part2S < 1) {
                part2S += 3;
            }
            while (part2S > 3) {
                part2S -= 3;
            }
            part2S += switch (you) {
                case ROCK -> 0;
                case PAPER -> 3;
                case SCISSORS -> 6;
            };

            return !part2 ? (you.score() + outcome) : (part2S);
        }).sum();
    }

}
