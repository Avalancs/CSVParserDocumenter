package org.avalancs.csvparser;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public abstract class FieldType {
    /** Name displayed in the documentation */
    String documentationName;
}
