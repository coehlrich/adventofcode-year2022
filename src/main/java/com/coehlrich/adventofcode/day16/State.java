package com.coehlrich.adventofcode.day16;

import java.util.Set;

public record State(String room, String elephant, Set<String> open, int minutes, int score) {

}
