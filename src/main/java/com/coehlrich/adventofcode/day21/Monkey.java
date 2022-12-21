package com.coehlrich.adventofcode.day21;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Monkey {

    static final Pattern CONSTANT = Pattern.compile("([a-z]{4}): (\\d+)");
    static final Pattern OPERATION = Pattern.compile("([a-z]{4}): ([a-z]{4}) ([+\\-*/]) ([a-z]{4})");

    long result(Map<String, Monkey> monkeys, long custom);

    String first();

    String second();

    public static Map.Entry<String, Monkey> parse(String line) {
        Monkey monkey = null;
        Matcher matcher;

        if ((matcher = CONSTANT.matcher(line)).matches()) {
            monkey = new ConstantMonkey(Integer.parseInt(matcher.group(2)));
        } else if ((matcher = OPERATION.matcher(line)).matches()) {
            String first = matcher.group(2);
            String second = matcher.group(4);
            monkey = switch (matcher.group(3).charAt(0)) {
                case '+' -> new AddMonkey(first, second);
                case '-' -> new SubtractionMonkey(first, second);
                case '*' -> new MultiplicationMonkey(first, second);
                case '/' -> new DivisionMonkey(first, second);
                default -> throw new IllegalArgumentException("Unexpected value: " + matcher.group(2).charAt(0));
            };
        }
        return Map.entry(matcher.group(1), monkey);
    }
}
