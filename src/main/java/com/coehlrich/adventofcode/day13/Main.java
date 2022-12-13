package com.coehlrich.adventofcode.day13;

import java.util.List;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        int part1 = 0;
        String[] split = input.split("\n\n");
        for (int i = 0; i < split.length; i++) {
            String pair = split[i];
            String[] lines = pair.split("\n");
            int comparison = compare(JsonParser.parseString(lines[0]), JsonParser.parseString(lines[1]));
            if (comparison < 0) {
                part1 += i + 1;
            }
        }
        input = input.replace("\n\n", "\n");
        JsonElement divider1 = JsonParser.parseString("[[2]]");
        JsonElement divider2 = JsonParser.parseString("[[6]]");

        input += "\n[[2]]\n[[6]]";
        List<JsonElement> output = input.lines().map(JsonParser::parseString).sorted(this::compare).toList();
//        System.out.println(output);
        return new Result(part1, (output.indexOf(divider1) + 1) * (output.indexOf(divider2) + 1));
    }

//    public Object parse(Reader string) {
//        if (string.matches("\\d+")) {
//            return Integer.parseInt(string);
//        } else {
//            string = string.substring(1, string.length() - 1);
//            if (string.isEmpty()) {
//                return List.of();
//            }
//            return Stream.of(string.split(",")).map(this::parse).toList();
//        }
//    }

    public int compare(Object first, Object second) {
        if (first instanceof JsonPrimitive fInteger && second instanceof JsonArray) {
            JsonArray array = new JsonArray();
            array.add(fInteger);
            return compare(array, second);
        } else if (first instanceof JsonArray && second instanceof JsonPrimitive sInteger) {
            JsonArray array = new JsonArray();
            array.add(sInteger);
            return compare(first, array);
        } else if (first instanceof JsonPrimitive fInteger && second instanceof JsonPrimitive sInteger) {
            return fInteger.getAsInt() < sInteger.getAsInt() ? -1
                    : fInteger.getAsInt() > sInteger.getAsInt() ? 1 : 0;
        } else if (first instanceof JsonArray fList && second instanceof JsonArray sList) {
            for (int i = 0; i < Math.min(fList.size(), sList.size()); i++) {
                int result = compare(fList.get(i), sList.get(i));
                if (result != 0) {
                    return result;
                }
            }
            return compare(new JsonPrimitive(fList.size()), new JsonPrimitive(sList.size()));
        }
        return 0;
    }

}
