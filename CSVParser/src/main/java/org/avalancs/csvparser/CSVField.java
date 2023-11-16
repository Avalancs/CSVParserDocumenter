package org.avalancs.csvparser;

public record CSVField(String name, FieldType fieldType, String description, boolean mandatory) {}
