package org.avalancs.csvparser.fieldtypes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public class DateFieldType extends FieldType {
    protected DateTimeFormatter formatter;
    protected DateType dateType;

    public enum DateType {
        LocalDate,
        LocalDateTime,
        ZonedDateTime
    }

    public DateFieldType(String documentationName, DateTimeFormatter formatter, DateType dateType) {
        super(documentationName);
        this.formatter = formatter;
        this.dateType = dateType;
    }

    public LocalDate parseLocalDate(String date) {
        return LocalDate.parse(date, formatter);
    }

    public LocalDateTime parseLocalDateTime(String date) {
        return LocalDateTime.parse(date, formatter);
    }

    public ZonedDateTime parseZonedDateTime(String date) {
        return ZonedDateTime.parse(date, formatter);
    }

    @Override
    public Object parse(String col) {
        return switch(dateType) {
            case LocalDate -> parseLocalDate(col);
            case LocalDateTime -> parseLocalDateTime(col);
            case ZonedDateTime -> parseZonedDateTime(col);
        };
    }
}
