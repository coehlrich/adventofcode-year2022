package com.coehlrich.adventofcode.day21;

import java.util.Map;

public record SubtractionMonkey(String first, String second) implements Monkey {

    @Override
    public long result(Map<String, Monkey> monkeys, long custom) {
        return monkeys.get(first).result(monkeys, custom) - monkeys.get(second).result(monkeys, custom);
    }

}
