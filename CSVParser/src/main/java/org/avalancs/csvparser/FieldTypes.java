package org.avalancs.csvparser;

import org.avalancs.csvparser.fieldtypes.DateFieldType;
import org.avalancs.csvparser.fieldtypes.FieldType;

import java.time.format.DateTimeFormatter;

public class FieldTypes {
    public static final FieldType STRING = new FieldType("Text"){};

    public static final FieldType NUMBER = new FieldType("Number") {
        @Override
        public Object parse(String col) {
            return Integer.parseInt(col);
        }
    };

    public static DateFieldType date(DateTimeFormatter formatter, DateFieldType.DateType dateType) {
        return new DateFieldType("Date", formatter, dateType);
    }
}
