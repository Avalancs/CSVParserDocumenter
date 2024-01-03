package org.avalancs.csvparser.example;

import org.avalancs.csvparser.CSVField;
import org.avalancs.csvparser.FieldTypes;
import org.avalancs.csvparser.MyCsvParser;
import org.avalancs.csvparser.fieldtypes.DateFieldType;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class WeatherCSVParser extends MyCsvParser {
    public static class Columns {
        public static final String ID = "id";
        public static final String DATA = "data";
        public static final String DATE = "date";
        public static final String PARAM = "param";
        public static final String SITE_ID = "siteid";
    }

    public WeatherCSVParser() {
        super(List.of(
            new CSVField(Columns.ID, "The database id", FieldTypes.NUMBER, true),
            new CSVField(Columns.DATA, "The temperature read from the device", FieldTypes.NUMBER, true),
            new CSVField(Columns.DATE, "The date the measurement was taken", FieldTypes.date(DateTimeFormatter.ISO_DATE, DateFieldType.DateType.LocalDate), true),
            new CSVField(Columns.PARAM, "Optional description of what kind of measurement was taken", FieldTypes.STRING, false),
            new CSVField(Columns.SITE_ID, "The site at where the measurement was taken (database ID)", FieldTypes.STRING, true)
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
