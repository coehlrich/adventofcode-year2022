package com.coehlrich.adventofcode.day20;

import java.util.ArrayList;
import java.util.List;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        List<IntegerWrapper> array = input.lines().mapToInt(Integer::parseInt).mapToObj(IntegerWrapper::new).toList();
        List<IntegerWrapper> mixed = new ArrayList<>(array);

        List<IntegerWrapper> key = new ArrayList<>(array);

        for (int i = 0; i < 9; i++) {
            mix(array, key, true);
        }

        return new Result(mix(array, mixed, false), mix(array, key, true));
    }

    public long mix(List<IntegerWrapper> array, List<IntegerWrapper> mixed, boolean part2) {
        for (int i = 0; i < array.size(); i++) {

            long newIndex = mixed.indexOf(array.get(i)) + array.get(i).get(part2);
            mixed.remove(array.get(i));
            newIndex = ((newIndex % mixed.size()) + mixed.size()) % mixed.size();

//            System.out.println(mixed);
//            if (newIndex != 0) {
            mixed.add((int) newIndex, array.get(i));
//                if (offset == 6184) {
//                    System.out.println(originalIndex);
//                    System.out.println(newIndex);
//                    System.out.println(mixed.indexOf(offset));
//                }

//                boolean same = false;
//                int j = 0;
//                while (!same && j < 5000) {
//                    Collections.rotate(mixed, 1);
//                    same = mixed.equals(mixed2);
//                    j++;
//                }
//                if (!same) {
//                    System.out.println(mixed);
//                    System.out.println(mixed2);
//                    return new Result(0, 0);
//                }
//                if (i % 100 == 0) {
//                    System.out.println(i);
//                }

//            } else {
//                mixed.add(array.getInt(i));
//            }
//            System.out.println(originalIndex + " (" + array.getInt(i) + ") -> " + newIndex);
//            System.out.println(mixed);
        }
        IntegerWrapper zero = mixed.stream().filter((wrapper) -> wrapper.num == 0).findFirst().get();
//        System.out.println(mixed2.indexOf(0));
//        System.out.println(mixed.indexOf(zero));
//        System.out.println((mixed2.indexOf(0) + 1000) % mixed2.size());
//        System.out.println((mixed.indexOf(zero) + 1000) % mixed.size());
//        System.out.println(mixed2.getInt((mixed2.indexOf(0) + 1000) % mixed2.size()));
//        System.out.println(mixed.get((mixed.indexOf(zero) + 1000) % mixed.size()));
        return mixed.get((mixed.indexOf(zero) + 1000) % mixed.size()).get(part2)
                + mixed.get((mixed.indexOf(zero) + 2000) % mixed.size()).get(part2)
                + mixed.get((mixed.indexOf(zero) + 3000) % mixed.size()).get(part2);
    }

}
