package com.coehlrich.adventofcode.day15;

public record Range(int start, int end) {

    public int getLength() {
        return end - start;
    }

}
