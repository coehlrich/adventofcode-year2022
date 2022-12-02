package com.coehlrich.adventofcode.day2;

import java.util.stream.Stream;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

public class Main implements Day {

    @Override
    public int day() {
        return 2;
    }

    @Override
    public Result execute(String input) {
        Match[] matches = input.lines().map((line) -> new Match(
                switch (line.charAt(0)) {
                    case 'A' -> RPS.ROCK;
                    case 'B' -> RPS.PAPER;
                    case 'C' -> RPS.SCISSORS;
                    default -> throw new IllegalStateException();
                },
                switch (line.charAt(2)) {
                    case 'X' -> RPS.ROCK;
                    case 'Y' -> RPS.PAPER;
                    case 'Z' -> RPS.SCISSORS;
                    default -> throw new IllegalStateException();
                })).toArray(Match[]::new);
        return new Result(
                Stream.of(matches)
                        .mapToInt((match) -> {
                            int outcome = 0;
                            if (match.you() == match.opponent()) {
                                outcome = 3;
                            } else if (match.you() == match.opponent()) {
                                outcome = 6;
                            }
                            return match.you().score() + outcome;
                        }).sum(),
                Stream.of(matches).mapToInt((match) -> {
                    // In this scenario rock is you lose, paper is you draw, and scissors is you win
                    int part2S = match.opponent().score() + match.you().score() - 2;
                    while (part2S < 1) {
                        part2S += 3;
                    }
                    while (part2S > 3) {
                        part2S -= 3;
                    }
                    part2S += switch (match.you()) {
                        case ROCK -> 0;
                        case PAPER -> 3;
                        case SCISSORS -> 6;
                    };
                    return part2S;
                }).sum());
    }

}
