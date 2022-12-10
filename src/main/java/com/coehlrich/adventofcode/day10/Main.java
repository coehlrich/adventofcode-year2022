package com.coehlrich.adventofcode.day10;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        Map<String, InstructionDefinition> instructionDefinitions = new HashMap<>();
        instructionDefinitions.put("addx", new InstructionDefinition((x, args) -> x + args.getInt(0), 2));
        instructionDefinitions.put("noop", new InstructionDefinition((x, args) -> x, 1));

        Instruction[] instructions = input.lines().map((line) -> {
            String[] split = line.split(" ");
            return new Instruction(instructionDefinitions.get(split[0]),
                    new IntArrayList(Stream.of(split).skip(1).map(Integer::parseInt).toList()));
        }).toArray(Instruction[]::new);

        int x = 1;
        int i = 0;
        IntList part1 = new IntArrayList();
        int stop = 0;
        Instruction instruction = null;
        StringBuilder part2 = new StringBuilder();
        for (int cycle = 1; cycle <= 240; cycle++) {
            if ((cycle - 1) % 40 == 0) {
                part2.append("\n");
            }
            if (Math.abs(x - ((cycle - 1) % 40)) <= 1) {
                part2.append('#');
            } else {
                part2.append('.');
            }
            if (stop <= 0) {
                instruction = instructions[i++ % instructions.length];
                stop = instruction.instruction().cycles();
                // System.out.println((cycle) + ": " + x + ", " + (x * cycle));
                // System.out.println(instruction.args());
            }

            if (stop > 0) {
                if (part1.size() * 40 + 20 <= cycle) {
                    part1.add(x);
                }
                stop--;
                if (stop <= 0) {
                    x = instruction.instruction().instruction().execute(x, instruction.args());
                }
            }

        }

        int part1S = 0;
        for (int j = 0; j < part1.size(); j++) {
//            System.out.println(j * 40 + 20);
//            System.out.println(part1.getInt(j));
//            System.out.println(part1.getInt(j) * (j * 40 + 20));
            part1S += part1.getInt(j) * (j * 40 + 20);
        }

        return new Result(part1S, part2);
    }

}
