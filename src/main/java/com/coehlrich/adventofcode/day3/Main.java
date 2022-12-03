package com.coehlrich.adventofcode.day3;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import it.unimi.dsi.fastutil.chars.CharArrayList;
import it.unimi.dsi.fastutil.chars.CharList;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        System.out.println(input.lines().allMatch((string) -> string.length() % 2 == 0));
        RuckSack[] sacks = input.lines().map((line) -> {
            int half = line.length() / 2;
            CharList first = new CharArrayList(line.substring(0, half).toCharArray());
            CharList second = new CharArrayList(line.substring(half, line.length()).toCharArray());
            return new RuckSack(first, second);
        }).toArray(RuckSack[]::new);

        int part1 = getResult(Stream.of(sacks).mapToInt((sack) -> {
            for (char first : sack.first()) {
                if (sack.second().contains(first)) {
                    return first;
                }
            }
            throw new IllegalArgumentException(sack.toString());
        }));

        List<List<RuckSack>> groups = new ArrayList<>();
        for (int i = 0; i < sacks.length; i += 3) {
            groups.add(List.of(sacks[i], sacks[i + 1], sacks[i + 2]));
        }

        int part2 = getResult(groups.stream().mapToInt((group) -> {
            List<CharList> allRackSacks = group.stream()
                    .map((sack) -> (CharList) new CharArrayList(
                            Stream.concat(sack.first().stream(), sack.second().stream()).toList()))
                    .toList();
            for (char character : allRackSacks.get(0)) {
                if (allRackSacks.stream().allMatch((list) -> list.contains(character))) {
                    return character;
                }
            }
            throw new IllegalArgumentException(allRackSacks.toString());
        }));
        return new Result(part1, part2);
    }

    public int getResult(IntStream chars) {
        return chars.map((character) -> {
            if (character >= 'a' && character <= 'z') {
                return (character - 'a') + 1;
            } else if (character >= 'A' && character <= 'Z') {
                return (character - 'A') + 27;
            }
            throw new IllegalArgumentException(String.valueOf((char) character));
        }).sum();
    }

}
