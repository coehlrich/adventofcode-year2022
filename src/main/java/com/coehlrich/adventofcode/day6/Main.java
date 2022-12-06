package com.coehlrich.adventofcode.day6;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

import it.unimi.dsi.fastutil.chars.CharArrayList;
import it.unimi.dsi.fastutil.chars.CharList;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        return new Result(find(input, 4), find(input, 14));
    }

    private int find(String input, int count) {
        CharList list = new CharArrayList(input.substring(0, count).toCharArray());
        int i;
        for (i = count; list.intStream().distinct().count() < count; i++) {
            list.add(input.charAt(i));
            list.removeChar(0);
        }
        return i;
    }

}
