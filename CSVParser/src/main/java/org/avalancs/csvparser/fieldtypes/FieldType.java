package org.avalancs.csvparser.fieldtypes;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public abstract class FieldType {
    /** Name displayed in the documentation */
    String documentationName;

    /** How to turn the String from the parsed CSV into the object you want. Returns the read-in column by default */
    public Object parse(String col) {
        return col;
    }

    /** Implement custom checks here and put a message for each error into the list */
    public List<String> customChecks(String col) {
        return null;
    }
}
