package org.avalancs.csvparser;

import org.avalancs.csvparser.fieldtypes.FieldType;

public record CSVField(String name, String description, FieldType fieldType, boolean mandatory) {}
