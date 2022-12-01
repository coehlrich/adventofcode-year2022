package com.coehlrich.adventofcode.day1;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

public class Main implements Day {

    @Override
    public int day() {
        return 1;
    }

    @Override
    public Result execute(String input) {
        String[] elves = input.split("\n\n");
        int[][] calories = Stream.of(elves).map((string) -> string.lines().mapToInt(Integer::parseInt).toArray())
                .toArray(int[][]::new);
        int max = Stream.of(calories).map(IntStream::of).mapToInt(IntStream::sum).max().getAsInt();

        int[] totalCalories = Stream.of(calories).map(IntStream::of).mapToInt(IntStream::sum).sorted().toArray();
        int size = totalCalories.length;
        int part2 = totalCalories[size - 1] + totalCalories[size - 2] + totalCalories[size - 3];
        return new Result(max, part2);

    }

}
