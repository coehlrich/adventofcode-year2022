package com.coehlrich.adventofcode.day21;

import java.util.Map;

public record ConstantMonkey(int constant) implements Monkey {

    @Override
    public long result(Map<String, Monkey> monkeys, long custom) {
        return constant;
    }

    @Override
    public String first() {
        return null;
    }

    @Override
    public String second() {
        return null;
    }

}
