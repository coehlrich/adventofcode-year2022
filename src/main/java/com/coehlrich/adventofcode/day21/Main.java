package com.coehlrich.adventofcode.day21;

import java.util.Map;
import java.util.stream.Collectors;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        Map<String, Monkey> map = input.lines().map(Monkey::parse)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        long part1 = map.get("root").result(map, -1);

        map.put("humn", new Human());

        Monkey first = map.get(map.get("root").first());
        Monkey second = map.get(map.get("root").second());

        System.out.println(first.result(map, 500000));
        System.out.println(first.result(map, 0));

        System.out.println(second.result(map, 10000));
        System.out.println(second.result(map, 0));

        boolean firstHasHuman = search(first, map);

        long other;
        if (firstHasHuman) {
            other = second.result(map, -1);
        } else {
            other = first.result(map, -1);
        }
        System.out.println(other);

        Monkey oMonkey;
        if (firstHasHuman) {
            oMonkey = first;
        } else {
            oMonkey = second;
        }

        return new Result(part1, Math.round(reverse(oMonkey, map, other)));
    }

    private double reverse(Monkey monkey, Map<String, Monkey> map, double result) {
        String first = monkey.first();
        String second = monkey.second();

        if (monkey instanceof Human) {
            return result;
        }

        if (first != null && second != null) {
            Monkey firstM = map.get(first);
            Monkey secondM = map.get(second);

            boolean firstHasHuman = search(firstM, map);

            if (search(firstM, map) && search(secondM, map)) {
                throw new RuntimeException("Multiple humans");
            }

//            if (!search(firstM, map) && !search(secondM, map)) {
//                throw new RuntimeException("No humans");
//            }

            long other;
            if (firstHasHuman) {
                other = secondM.result(map, -1);
            } else {
                other = firstM.result(map, -1);
            }

            double value = 0l;
            if (monkey instanceof AddMonkey) {
                value = result - other;
            } else if (monkey instanceof SubtractionMonkey) {
                value = result + other;
            } else if (monkey instanceof MultiplicationMonkey) {
//                if (result % other != 0) {
//                    throw new RuntimeException(result + " " + other);
//                }
                value = result / other;
            } else if (monkey instanceof DivisionMonkey) {
                value = result * other;
            }

            Monkey hasH = firstHasHuman ? firstM : secondM;

            return reverse(hasH, map, value);
        }
        return 0l;
    }

    private boolean search(Monkey monkey, Map<String, Monkey> map) {
        if (monkey instanceof Human) {
            return true;
        }

        String first = monkey.first();
        String second = monkey.second();

//        if (first != null && second != null && search(map.get(first), map) && search(map.get(second), map)) {
//            throw new RuntimeException("Multiple humans");
//        }

        if (first != null && search(map.get(first), map)) {
            return true;
        }

        if (second != null && search(map.get(second), map)) {
            return true;
        }
        return false;
    }

}
