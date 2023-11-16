package org.avalancs.csvparser;

import lombok.Getter;
import org.apache.commons.csv.CSVParser;

import java.util.List;

@Getter
public abstract class MyCsvParser {
    protected List<CSVField> fields;

    protected MyCsvParser(List<CSVField> fields) {
        this.fields = fields;
    }

    /** The name of the CSV (the chapter name inside the documentation) */
    public abstract String getName();

    /** The text for the "Overview" chapter of the documentation */
    public abstract String getDescription();

    /**
     * Return true to not include this CSV in documentation
     */
    protected boolean skipDocumentation() {
        return false;
    }

    public CSVParser getParser() {
        return null; // TODO
    }
}
