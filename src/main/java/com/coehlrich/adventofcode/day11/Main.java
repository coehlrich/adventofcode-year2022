package com.coehlrich.adventofcode.day11;

import java.util.List;
import java.util.stream.Stream;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

public class Main implements Day {

    @Override
    public Result execute(String input) {

        long modulo = 1;

        for (int prime : Stream.of(input.split("\n\n")).mapToInt(Monkey::getNeededPrime).toArray()) {
            modulo *= prime;
        }

        List<Monkey> monkeys = Stream.of(input.split("\n\n")).map(Monkey::parse).toList();
        List<Monkey> monkeys2 = Stream.of(input.split("\n\n")).map(Monkey::parse).toList();

        return new Result(calculate(monkeys, 20, false, modulo), calculate(monkeys2, 10000, true, modulo));
//        return new Result(calculate(monkeys, 20, false, modulo), 0);
    }

    public long calculate(List<Monkey> monkeys, int rounds, boolean part2, long modulo) {
        LongList part1 = new LongArrayList(new long[monkeys.size()]);
        for (int i = 0; i < rounds; i++) {
            for (int j = 0; j < monkeys.size(); j++) {
                Monkey monkey = monkeys.get(j);
                part1.set(j, part1.getLong(j) + monkey.throwItems(monkeys, part2, modulo));
            }
//            if (i == 0 || i == 19 || i % 1000 == 999) {
//                System.out.println("Round: " + i);
//                System.out.println(part1);
//                for (int j = 0; j < monkeys.size(); j++) {
//                    System.out.println("monkey " + j + ": " + monkeys.get(j).items());
//                }
//            }
        }

//        System.out.println(part1);
        long max = part1.longStream().max().getAsLong();
        part1.rem(max);
        long secondMax = part1.longStream().max().getAsLong();
        return max * secondMax;
    }

}
