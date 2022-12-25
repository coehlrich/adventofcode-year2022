package com.coehlrich.adventofcode.day25;

import com.coehlrich.adventofcode.Day;
import com.coehlrich.adventofcode.Result;

public class Main implements Day {

    @Override
    public Result execute(String input) {
        long sum = input.lines().mapToLong((line) -> {
            long value = 0;
            String reversed = new StringBuilder(line).reverse().toString();
            for (int i = 0; i < reversed.length(); i++) {
                long power = (long) Math.pow(5, i);
                value += switch (reversed.charAt(i)) {
                    case '2' -> 2;
                    case '1' -> 1;
                    case '0' -> 0;
                    case '-' -> -1;
                    case '=' -> -2;
                    default -> throw new IllegalArgumentException("Unexpected value: " + reversed.charAt(i));
                } * power;
            }
//            System.out.println(line + " = " + value);
            return value;
        }).sum();

//        System.out.println(sum);
//
//        for (int i = 1; i <= 10; i++) {
//            System.out.println(i + " = " + getSNAFU(i));
//        }
//
//        System.out.println(15 + " = " + getSNAFU(15));
//        System.out.println(20 + " = " + getSNAFU(20));
//        System.out.println(2022 + " = " + getSNAFU(2022));
//        System.out.println(12345 + " = " + getSNAFU(12345));
//        System.out.println(314159265 + " = " + getSNAFU(314159265));
        return new Result(getSNAFU(sum), "N/A");
    }

    private String getSNAFU(long sum) {
        StringBuilder line = new StringBuilder();
        for (long num = sum; num > 0l; num /= 5) {
            long value = num % 5;
            if (value >= 0 && value <= 2) {
                line.append(value);
            } else if (value == 3) {
                line.append("=");
            } else if (value == 4) {
                line.append("-");
            }
//            line.append(switch (value) {
//                case 0, 1, 2 -> value;
//                case 3 -> "=";
//                case 4 -> "-";
//                default -> throw new IllegalArgumentException("Unexpected value: " + value);
//            });
            if (value >= 3) {
                num += 5;
            }
//            System.out.println(line.toString());
        }
        return line.reverse().toString();
    }

}
