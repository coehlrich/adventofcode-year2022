package com.coehlrich.adventofcode.day4;

import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import it.unimi.dsi.fastutil.ints.IntOpenHashSet;
import it.unimi.dsi.fastutil.ints.IntSet;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        Pairs[] pairs = input.lines().map((line) -> {
            String[] parts = line.split("[-,]");
            int[] integers = Stream.of(parts).mapToInt(Integer::parseInt).toArray();
            IntSet range1 = new IntOpenHashSet(IntStream.rangeClosed(integers[0], integers[1]).toArray());
            IntSet range2 = new IntOpenHashSet(IntStream.rangeClosed(integers[2], integers[3]).toArray());
            return new Pairs(range1, range2);
        }).toArray(Pairs[]::new);
        int part1 = Stream.of(pairs).mapToInt((pair) -> {
            IntSet range1 = pair.range1();
            IntSet range2 = pair.range2();
            if (range1.containsAll(range2) || range2.containsAll(range1)) {
                return 1;
            } else {
                return 0;
            }
        }).sum();
        int part2 = Stream.of(pairs).mapToInt((pair) -> {
            IntSet range1 = pair.range1();
            IntSet range2 = pair.range2();
            if (range1.intStream().anyMatch(range2::contains)) {
                return 1;
            } else {
                return 0;
            }
        }).sum();
        return new Result(part1, part2);
    }

}
