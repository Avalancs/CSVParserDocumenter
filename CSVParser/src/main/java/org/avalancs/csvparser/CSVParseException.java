package org.avalancs.csvparser;

import lombok.Getter;

import java.nio.file.Path;
import java.util.List;
import java.util.Map;

@Getter
public class CSVParseException extends RuntimeException {
    final Path file;
    final Map<Integer, Map<String, List<String>>> errors;

    public CSVParseException(Path file, Map<Integer, Map<String, List<String>>> errors) {
        this.file = file;
        this.errors = errors;
    }
}
