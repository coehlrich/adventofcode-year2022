package com.coehlrich.adventofcode.day20;

public class IntegerWrapper {
    public final long num;

    public IntegerWrapper(long num) {
        this.num = num;
    }

    public long get(boolean part2) {
        return num * (part2 ? 811589153 : 1);
    }
}
