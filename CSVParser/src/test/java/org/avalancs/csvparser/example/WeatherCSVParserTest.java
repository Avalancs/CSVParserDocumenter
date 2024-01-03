package org.avalancs.csvparser.example;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;

public class WeatherCSVParserTest {
    @Test
    public void testParser() throws IOException {
        WeatherCSVParser parser = new WeatherCSVParser();
        parser.streamRead(Paths.get("src/test/resources/weather.data.csv"), row -> {
            System.out.println();
            row.forEach((k, v) -> System.out.println(k + ": " + v));
        });
    }
}
