module com.coehlrich.adventofcode.year2022 {

    requires joptsimple;
    requires java.net.http;

    uses com.coehlrich.adventofcode.Day;

    provides com.coehlrich.adventofcode.Day with
            com.coehlrich.adventofcode.day1.Main,
            com.coehlrich.adventofcode.day2.Main;
}