package com.coehlrich.adventofcode.day17;

import java.util.ArrayList;
import java.util.List;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        input = input.replace("\n", "");

//        long singleCount = simulate(input.length() * 4, input);
//        long doubleCount = simulate(input.length() * 4 * 2, input);
//        System.out.println(singleCount + doubleCount * (6250000000l - 1l));
////        long overCount = simulate(input.length() * 4 + );
//        System.out.println(input.length() * 4);
//        System.out.println(1000000000000l / (input.length() * 4));
//        System.out.println(singleCount);
//        for (int i = 2; i < 100; i++) {
//            long newCount = simulate(input.length() * 4 * i, input);
//            System.out.println(newCount - singleCount);
//            singleCount = newCount;
//        }
//        System.out.println(loop);
//        loop = 0;
//        long doubleCount = simulate(input.length() * 5 * 2, input);
//        System.out.println(loop);
//        loop = 0;
//        long tripleCount = simulate(input.length() * 5 * 3, input);
//        System.out.println(loop);
//        loop = 0;
//        long quadCount = simulate(input.length() * 5 * 4, input);
//        System.out.println(loop);
//        loop = 0;
//        long fiveCount = simulate(input.length() * 5 * 5, input);
//        System.out.println(loop);
//        loop = 0;
//        System.out.println(singleCount);
//        System.out.println(doubleCount - singleCount);
//        System.out.println(tripleCount - doubleCount);
//        System.out.println(quadCount - tripleCount);
//        System.out.println(fiveCount - quadCount);
        return new Result(simulate(input, false), simulate(input, true));
//        return new Result(simulate(2022, input), (long) simulate(input.length() * 5 * 2, input) * ());
    }

    private long simulate(String input, boolean part2) {
        List<boolean[]> rows = new ArrayList<>();
        List<boolean[][]> shapes = List.of(
                new boolean[][] {{ true, true, true, true }},
                new boolean[][] {
                        { false, true, false },
                        { true, true, true },
                        { false, true, false }
                },
                new boolean[][] {
                        { false, false, true },
                        { false, false, true },
                        { true, true, true }
                },
                new boolean[][] {
                        { true },
                        { true },
                        { true },
                        { true }
                },
                new boolean[][] {
                        { true, true },
                        { true, true }
                });

        int shapesI = 0;
        int dirI = 0;
//        int maxDir = 0;
        Value state = new Value(0, 0);
        Int2ObjectMap<Value> states = new Int2ObjectOpenHashMap<>();

//        for (long i = 0; i < (part2 ? 1000000000000l : 2022l); i++) {
        for (long i = 0; i < (part2 ? 100_000_000_000l : 2022l); i++) {
            if (part2) {
                Value newState = new Value(dirI, rows.size());
                Value value = cache(states, state, newState, 10, i);
                if (value.dir() == -1) {
                    return value.score();
                }
                state = value;
            }
            boolean[][] shape = shapes.get(shapesI);
            shapesI++;
            shapesI %= shapes.size();
            boolean[] bottom = shape[shape.length - 1];
            int x = 2;
            int y = rows.size() + 4;
//            if (i < 10) {
//                System.out.print(i + ": ");
//            }
            boolean fits = false;
            do {
                y--;
                char dir = input.charAt(dirI);
                dirI++;
                dirI %= input.length();
//                maxDir = Math.max(dirI, maxDir);
//                if (i < 10) {
//                    System.out.print(dir);
//                }
                dir: switch (dir) {
                    case '<' -> {
                        if (x > 0) {
                            for (int j = 0; j < shape[0].length; j++) {
                                if (!fits(getSide(shape, j), getSide(rows, y, x + j - 1, shape.length), 0)) {
                                    break dir;
                                }
                            }
                            x--;
                        }
                    }
                    case '>' -> {
                        if (x + bottom.length < 7) {
                            for (int j = 0; j < shape[0].length; j++) {
                                if (!fits(getSide(shape, j),
                                        getSide(rows, y, j + x + 1, shape.length), 0)) {
                                    break dir;
                                }
                            }
                            x++;
                        }
                    }
                    default -> throw new IllegalStateException(String.valueOf(dir));
                }
                fits = y > 0 && y <= rows.size();
                if (fits) {
                    for (int j = 0; j < shape.length; j++) {
                        if (j + y <= rows.size()) {
                            fits &= fits(shape[shape.length - j - 1], rows.get(y + j - 1), x);
                            if (!fits) {
                                break;
                            }
                        }
                    }
                }
            } while (y > rows.size() || fits);
            for (int j = 0; j < shape.length; j++) {
                boolean[] row = shape[shape.length - j - 1];
                if (j + y >= rows.size()) {
                    rows.add(new boolean[7]);
                }
                boolean[] pitR = rows.get(j + y);
                for (int k = 0; k < row.length; k++) {
                    if (pitR[k + x] && row[k]) {
                        System.out.println("Already filled");
                    }
                    pitR[k + x] |= row[k];
                }
            }

//            if (i < 10) {
//                System.out.println();
//                for (int j = rows.size() - 1; j >= 0; j--) {
//                    System.out.print('|');
//                    for (int k = 0; k < rows.get(j).length; k++) {
//                        System.out.print(rows.get(j)[k] ? '#' : '.');
//                    }
//                    System.out.print('|');
//                    System.out.println();
//                }
//                System.out.println();
//                System.out.println();
//            }
//            if (dirI % input.length() == 0 && shapesI % shapes.size() == 0) {
//                loop = i;
//            }
        }
//        System.out.println(states.size());
        return rows.size();
    }

    private Value cache(Int2ObjectMap<Value> cache, Value previous, Value newValue, long count, long i) {
        if (i % count == 0 && i != 0) {
            cache.put(previous.dir(), new Value(newValue.dir(), newValue.score() - previous.score()));

            if (cache.containsKey(newValue.dir())) {
                long score = newValue.score();
                Value subPrev = new Value(0, 0);
                Int2ObjectMap<Value> subCache = new Int2ObjectOpenHashMap<>();
                while (i < 1_000_000_000_000l) {
                    newValue = cache.get(newValue.dir());
//                    System.out.println(newValue);
                    i += count;
                    score += newValue.score();

                    Value newSubValue = new Value(newValue.dir(), score);
                    Value value = cache(subCache, subPrev, newSubValue, count == 10 ? 100 : count * 100l, i);
                    if (value.dir() == -1) {
                        return value;
                    }
                    subPrev = value;

                    previous = newValue;
                }
                return new Value(-1, score);
            }
            return newValue;
        }
        return previous;
    }

    private boolean fits(boolean[] from, boolean[] into, int x) {
        for (int i = 0; i < from.length; i++) {
            if (into[i + x] && from[i]) {
                return false;
            }
        }
        return true;
    }

    private boolean[] getSide(List<boolean[]> rows, int y, int x, int height) {
        boolean[] side = new boolean[height];
        for (int i = 0; i < height; i++) {
            if (i + y < rows.size()) {
                side[i] = rows.get(i + y)[x];
            } else {
                side[i] = false;
            }
        }
        return side;
    }

    private boolean[] getSide(boolean[][] rows, int x) {
        boolean[] side = new boolean[rows.length];
        for (int i = 0; i < rows.length; i++) {
            side[i] = rows[rows.length - i - 1][x];
        }
        return side;
    }

}
