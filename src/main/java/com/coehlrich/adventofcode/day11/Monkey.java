package com.coehlrich.adventofcode.day11;

import java.util.List;
import java.util.function.LongPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import it.unimi.dsi.fastutil.longs.Long2LongFunction;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;

public record Monkey(LongList items, Long2LongFunction operation, LongPredicate test,
        int ifTrue, int ifFalse) {

    private static final Pattern NUMBER = Pattern.compile("\\d+");
    private static final Pattern STARTING_ITEMS = Pattern.compile("  Starting items: ((?:,? ?\\d+)+)");
    private static final Pattern OPERATION = Pattern.compile("  Operation: new = old ([+*]) (old|\\d+)");
    private static final Pattern TEST = Pattern.compile("  Test: divisible by (\\d+)");
    private static final Pattern IF_TRUE = Pattern.compile("    If true: throw to monkey (\\d+)");
    private static final Pattern IF_FALSE = Pattern.compile("    If false: throw to monkey (\\d+)");

    public static Monkey parse(String mString) {
        String[] split = mString.lines().toArray(String[]::new);
        try {
            LongList items = null;
            Long2LongFunction operation = null;
            LongPredicate test = null;
            int ifTrue = -1;
            int ifFalse = -1;
            for (String line : split) {
                Matcher matcher;
                if ((matcher = STARTING_ITEMS.matcher(line)).matches()) {
                    items = new LongArrayList();
                    String group = matcher.group(1);
                    // if java matches multiple parts with the same group then only the last gets
                    // kept
                    Matcher numberMatcher = NUMBER.matcher(group);
                    while (numberMatcher.find()) {
                        items.add(Integer.parseInt(numberMatcher.group()));
                    }
                } else if ((matcher = OPERATION.matcher(line)).matches()) {
                    char op = matcher.group(1).charAt(0);
                    String value = matcher.group(2);
                    try {
                        long valueInt = Integer.parseInt(value);
                        if (op == '+') {
                            operation = ((old) -> old + valueInt);
                        } else {
                            operation = ((old) -> old * valueInt);
                        }
                    } catch (NumberFormatException e) {
                        if (op == '+') {
                            operation = ((old) -> old + old);
                        } else {
//                            operation = ((old) -> old);
                            operation = ((old) -> old * old);
                        }
                    }
                } else if ((matcher = TEST.matcher(line)).matches()) {
                    long divisible = Integer.parseInt(matcher.group(1));
                    test = ((worry) -> worry % divisible == 0);
                } else if ((matcher = IF_TRUE.matcher(line)).matches()) {
                    ifTrue = Integer.parseInt(matcher.group(1));
                } else if ((matcher = IF_FALSE.matcher(line)).matches()) {
                    ifFalse = Integer.parseInt(matcher.group(1));
                }
            }
            return new Monkey(items, operation, test, ifTrue, ifFalse);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return null;
    }

    public int throwItems(List<Monkey> monkeys, boolean part2, long modulo) {
        int inspected = items.size();
        for (long item : items) {
            long worry = operation.apply(item) / (part2 ? 1 : 3);
            worry %= modulo;
            int monkey = test.test(worry) ? ifTrue : ifFalse;
//            System.out.println(test.test(worry));
            monkeys.get(monkey).items.add(worry);
        }
        items.clear();

        return inspected;
    }

    public static int getNeededPrime(String mString) {
        String[] split = mString.lines().toArray(String[]::new);
        for (String line : split) {
            Matcher matcher;
            if ((matcher = TEST.matcher(line)).matches()) {
                return Integer.parseInt(matcher.group(1));
            }
        }
        throw new IllegalStateException();
    }

}
