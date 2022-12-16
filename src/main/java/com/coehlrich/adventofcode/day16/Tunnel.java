package com.coehlrich.adventofcode.day16;

public record Tunnel(int cost, String end) {
    public static Tunnel parse(String room) {
        return new Tunnel(1, room);
    }
}
