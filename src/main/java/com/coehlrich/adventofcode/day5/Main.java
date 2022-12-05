package com.coehlrich.adventofcode.day5;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import it.unimi.dsi.fastutil.chars.CharArrayList;
import it.unimi.dsi.fastutil.chars.CharList;
import it.unimi.dsi.fastutil.chars.CharStack;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class Main implements Day {

    private static final Pattern INSTRUCTION = Pattern.compile("move (\\d+) from (\\d+) to (\\d+)");

    @Override
    public Result execute(String input) {
        Int2ObjectMap<CharStack> map = new Int2ObjectOpenHashMap<>();
        String[] parts = input.split("\n\n");

        String[] first = parts[0].lines().toArray(String[]::new);
        for (int i = first.length - 1; i >= 0; i--) {
            String line = first[i];
            for (int j = 0; j < line.length() / 4 + 1; j++) {
                char character = line.charAt(j * 4 + 1);
                if (character >= 'A' && character <= 'Z') {
                    map.computeIfAbsent(j + 1, (key) -> new CharArrayList())
                            .push(character);
                }
            }
        }

        Instruction[] instructions = parts[1].lines().map((line) -> {
            Matcher matcher = INSTRUCTION.matcher(line);
            matcher.matches();
            int count = Integer.parseInt(matcher.group(1));
            int from = Integer.parseInt(matcher.group(2));
            int to = Integer.parseInt(matcher.group(3));
            return new Instruction(count, from, to);
        }).toArray(Instruction[]::new);

        return new Result(solve(instructions, new Int2ObjectOpenHashMap<>(
                map.int2ObjectEntrySet().stream().map((entry) -> {
                    return Map.entry(entry.getKey(), new CharArrayList((CharArrayList) entry.getValue()));
                }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))), false),
                solve(instructions, new Int2ObjectOpenHashMap<>(map), true));
    }

    private String solve(Instruction[] instructions, Int2ObjectMap<CharStack> map, boolean part2) {
        for (Instruction instruction : instructions) {
            CharStack from = map.get(instruction.from());
            CharStack to = map.get(instruction.to());
            int count = instruction.count();
            CharList list = new CharArrayList();
            for (int i = 0; i < count; i++) {
                list.add(from.popChar());
            }

            if (part2) {
                CharList temp = new CharArrayList();
                for (char character : list) {
                    temp.add(0, character);
                }
                list = temp;
            }

            for (char character : list) {
                to.push(character);
            }
        }

        return new String(new CharArrayList(map.keySet().intStream().sorted().mapToObj((integer) -> {
            return map.get(integer).peekChar(0);
        }).toList()).toCharArray());
    }

}
