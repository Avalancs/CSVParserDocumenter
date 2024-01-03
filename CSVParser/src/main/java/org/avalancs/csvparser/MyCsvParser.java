package org.avalancs.csvparser;

import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

@Getter
public abstract class MyCsvParser {
    protected List<CSVField> fields;

    /** The CSV file format used by the Apache CSV parser */
    @Setter
    protected CSVFormat csvFormat;

    protected MyCsvParser(List<CSVField> fields) {
        this(fields, CSVFormat.DEFAULT);
    }

    protected MyCsvParser(@NonNull List<CSVField> fields, @NonNull CSVFormat csvFormat) {
        this.fields = fields;
        this.csvFormat = csvFormat;
        // pass header information to csv format
        // setHeader does not have a List<String> variant so need to cast to array to fulfill varargs...
        this.csvFormat = this.csvFormat.builder()
            .setAllowMissingColumnNames(true)
            .setIgnoreHeaderCase(true)
            .setTrim(true) // TODO: should we use this?
            .setHeader(fields.stream().map(CSVField::name).toList().toArray(new String[0]))
            .setSkipHeaderRecord(true) // do not return the first row with the headers
            .build();
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

    public CSVParser getParser(Reader reader) throws IOException {
        return new CSVParser(reader, csvFormat);
    }

    public void streamRead(Path file, Consumer<Map<String, Object>> function) throws IOException {
        int lineIndex = 1;
        // Read through file and check each row for errors
        Map<Integer, Map<String, List<String>>> errors = new HashMap<>();
        try(CSVParser parser = getParser(Files.newBufferedReader(file, StandardCharsets.UTF_8))) {
            for(CSVRecord row : parser) {
                Map<String, List<String>> rowErrors = checkRow(row);
                if(rowErrors != null && !rowErrors.isEmpty()) {
                    errors.put(lineIndex, rowErrors);
                }
                lineIndex++;
            }
        }
        if(!errors.isEmpty()) {
            throw new CSVParseException(file, errors);
        }

        // Actually parse columns and run the passed-in function for each row
        try(CSVParser parser = getParser(Files.newBufferedReader(file, StandardCharsets.UTF_8))) {
            for(CSVRecord row : parser) {
                function.accept(parseRow(row));
            }
        }
    }

    protected Map<String, Object> parseRow(CSVRecord row) {
        Map<String, Object> parsed = new HashMap<>();
        for(CSVField i : fields) {
            String col = row.get(i.name());
            parsed.put(i.name(), (col != null) ? i.fieldType().parse(col) : null);
        }
        return parsed;
    }

    protected Map<String, List<String>> checkRow(CSVRecord row) {
        Map<String, List<String>> errors = null;

        for(CSVField i : fields) {
            String col = row.get(i.name());

            // if mandatory row it should not be null
            if(col == null && i.mandatory()) {
                errors = addErrorToMap(errors, i.name(), "Cannot be null!");
                continue;
            }

            // custom checks
            if(col != null) {
                List<String> customErrors = i.fieldType().customChecks(col);
                if(customErrors != null && !customErrors.isEmpty()) {
                    errors = addErrorToMap(errors, i.name(), customErrors);
                }
            }
        }

        return errors;
    }

    protected Map<String, List<String>> addErrorToMap(Map<String, List<String>> errors, String column, String error) {
        // initialize map for the first time
        if(errors == null) {
            errors = new HashMap<>();
        }

        if(!errors.containsKey(column)) {
            errors.put(column, new ArrayList<>());
        }
        errors.get(column).add(error);

        return errors;
    }

    protected Map<String, List<String>> addErrorToMap(Map<String, List<String>> errors, String column, List<String> errorList) {
        // initialize map for the first time
        if(errors == null) {
            errors = new HashMap<>();
        }

        if(!errors.containsKey(column)) {
            errors.put(column, new ArrayList<>());
        }
        errors.get(column).addAll(errorList);

        return errors;
    }
}
