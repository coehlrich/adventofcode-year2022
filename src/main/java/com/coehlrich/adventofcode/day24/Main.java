package com.coehlrich.adventofcode.day24;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import com.coehlrich.adventofcode.util.Direction;
import com.coehlrich.adventofcode.util.Point2;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        List<Blizzard> blizzards = new ArrayList<>();

        String[] lines = input.split("\n");
        lines = Stream.of(lines).limit(lines.length - 1).skip(1).toArray(String[]::new);

        int maxY = lines.length;
        int maxX = lines[0].length() - 2;
        for (int y = 0; y < lines.length; y++) {
            for (int x = 1; x < lines[y].length() - 1; x++) {
                char character = lines[y].charAt(x);
                Direction direction = switch (character) {
                    case '^' -> Direction.UP;
                    case '>' -> Direction.RIGHT;
                    case 'v' -> Direction.DOWN;
                    case '<' -> Direction.LEFT;
                    default -> null;
                };
                if (direction != null) {
                    blizzards.add(new Blizzard(new Point2(x - 1, y), direction));
                }
            }
        }

        System.out.println(blizzards);

        List<Elf> states = new ArrayList<>();
        states.add(new Elf(new Point2(0, -1), 0));

        int min = 0;
        int part1 = 0;
        boolean finished = false;
        while (!finished) {
            min++;
            blizzards = blizzards.stream().map((blizzard) -> blizzard.move(maxY, maxX)).toList();

            Elf end = null;
            Set<Elf> newStates = new HashSet<>();
            for (Elf state : states) {
                newStates.add(state);
                for (Direction direction : Direction.values()) {
                    int goal = state.goal();
                    Point2 newP = direction.offset(state.pos());
                    if (newP.x() == maxX - 1 && newP.y() == maxY) {
                        if (part1 == 0) {
                            part1 = min;
                        }

                        if (goal % 2 == 0) {
                            if (goal == 2) {
                                finished = true;
                                break;
                            }
                            end = new Elf(newP, goal + 1);
                            break;
                        }
                    }

                    if (newP.x() == 0 && newP.y() == -1) {
                        if (goal % 2 == 1) {
                            goal++;
                            end = new Elf(newP, goal);
                            break;
                        }
                    }

                    if (newP.x() >= 0 && newP.x() < maxX && newP.y() >= 0 && newP.y() < maxY) {
                        newStates.add(new Elf(newP, state.goal()));
                    }
                }
            }

            if (!finished) {
                if (end != null) {
                    states = List.of(end);
                } else {
                    List<Point2> blizzardPos = blizzards.stream().map(Blizzard::point).toList();
                    states = newStates.stream().filter((state) -> !blizzardPos.contains(state.pos())).toList();
                }
            }
        }
        return new Result(part1, min);
    }

}
