package com.coehlrich.adventofcode.day8;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        int[][] map = input.lines().map((line) -> {
            IntList list = new IntArrayList();
            for (char character : line.toCharArray()) {
                list.add(Integer.parseInt(String.valueOf(character)));
            }
            return list.toIntArray();
        }).toArray(int[][]::new);
        boolean[][] seeMap = new boolean[map[0].length][map.length];

        for (int y = 0; y < map.length; y++) {
            seeMap[0][y] = true;
            seeMap[map[0].length - 1][y] = true;
        }

        for (int x = 0; x < map.length; x++) {
            seeMap[x][0] = true;
            seeMap[x][map.length - 1] = true;
        }

        int count = (map.length * 2 + map[0].length * 2) - 4;
        for (int y = 1; y < map[0].length - 1; y++) {
            count += countTrees(map, seeMap, y, 0, 0, 1, 0, false);
            count += countTrees(map, seeMap, y, map.length - 1, 0, -1, 0, false);
        }

        for (int x = 1; x < map.length - 1; x++) {
            count += countTrees(map, seeMap, 0, x, 1, 0, 0, false);
            count += countTrees(map, seeMap, map[0].length - 1, x, -1, 0, 0, false);
        }

        int[][] part2 = new int[map.length][map.length];
        int max = 0;
        for (int y = 1; y < map.length - 1; y++) {
            for (int x = 1; x < map.length - 1; x++) {
                int level = map[y][x];
                int value =
                        countTrees(map, null, y - 1, x, -1, 0, level, true) *
                        countTrees(map, null, y + 1, x,  1, 0, level, true) *
                        countTrees(map, null, y, x - 1, 0, -1, level, true) *
                        countTrees(map, null, y, x + 1, 0,  1, level, true);
                max = Math.max(value, max);
//                int test = countTrees(map, null, y - 1, x, -1, 0, level, true);
                part2[y][x] = value;
            }
        }
//        for (int y = 0; y < map.length; y++) {
//            for (int x = 0; x < map.length; x++) {
//                System.out.print(part2[y][x]);
//            }
//            System.out.println();
//        }
        return new Result(count, max);
    }

    public int countTrees(int[][] map, boolean[][] seeMap, int y, int x, int offsetY, int offsetX, int level,
            boolean useLevel) {
        int count = 0;
        for (; y < map[0].length && x < map.length && y >= 0 && x >= 0; y += offsetY, x += offsetX) {
            if (seeMap == null || !seeMap[y][x]) {
                // originally swapped both signs
                if (useLevel ? map[y][x] < level : map[y][x] > level) {
                    count++;
                    if (seeMap != null) {
                        seeMap[y][x] = true;
                    }
                } else if (useLevel) {
                    count++;
                    break;
                }
            }
            if (!useLevel) {
                level = Math.max(level, map[y][x]);
            }
        }
        return count;
    }

}
