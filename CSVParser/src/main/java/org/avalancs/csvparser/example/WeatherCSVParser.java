package org.avalancs.csvparser.example;

import org.avalancs.csvparser.CSVField;
import org.avalancs.csvparser.FieldTypes;
import org.avalancs.csvparser.MyCsvParser;

import java.util.List;

public class WeatherCSVParser extends MyCsvParser {
    public WeatherCSVParser() {
        super(List.of(
            new CSVField("id", FieldTypes.NUMBER, "The database id", true),
            new CSVField("data", FieldTypes.NUMBER, "The temperature read from the device", true),
            new CSVField("date", FieldTypes.DATE, "The date the measurement was taken", true),
            new CSVField("param", FieldTypes.STRING, "Optional description of what kind of measurement was taken", false),
            new CSVField("siteid", FieldTypes.STRING, "The site at where the measurement was taken (database ID)", true)
        ));
    }

    @Override
    public String getName() {
        return "Weather report CSV export";
    }

    @Override
    public String getDescription() {
        return "Temperature measurements taken at particular times at particular sites";
    }
}
