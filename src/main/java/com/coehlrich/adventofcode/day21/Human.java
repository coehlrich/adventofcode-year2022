package com.coehlrich.adventofcode.day21;

import java.util.Map;

public class Human implements Monkey {

    @Override
    public long result(Map<String, Monkey> monkeys, long custom) {
        return custom;
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
