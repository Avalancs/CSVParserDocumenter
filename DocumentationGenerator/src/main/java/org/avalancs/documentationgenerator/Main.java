package org.avalancs.documentationgenerator;

import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import lombok.extern.slf4j.Slf4j;
import org.asciidoctor.*;
import org.avalancs.csvparser.MyCsvParser;

import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class Main {
    public static final Path csvPebbleTemplate = Paths.get("template/pebble/csv.adoc.peb");
    public static final Path csvOutputAdoc = Paths.get("template/generated/csvparsers.adoc");
    public static final Path indexAdocFile = Paths.get("template/index.adoc");
    public static final Path outputDir = Paths.get("template/generated");

    public static void main(String[] args) {
        PebbleEngine pebble = createPebbleEngine();
        PebbleTemplate template = pebble.getTemplate(csvPebbleTemplate.toString());

        // Find all derivatives of CSVParser
        try (ScanResult scanResult = new ClassGraph().enableAllInfo().acceptPackages("org.avalancs.csvparser").scan()) {
            final Map<String, Object> pebbleVars = new HashMap<>();
            ClassInfoList widgetClasses = scanResult.getSubclasses("org.avalancs.csvparser.MyCsvParser");

            log.trace("Found the following csv parsers: " + String.join(", ", widgetClasses.getNames()));

            List<Class<?>> parsers = widgetClasses.loadClasses();

            // Create template/generated directory, in case it does not exist (it's ignored in git so won't exist on first clone)
            try {
                Files.createDirectories(csvOutputAdoc.getParent());
            } catch(IOException e) {
                log.error("Could not create directory for generated template!", e);
                System.exit(-1);
            }

            // over-write or create asciidoc template
            try(BufferedWriter output = Files.newBufferedWriter(csvOutputAdoc, StandardOpenOption.TRUNCATE_EXISTING, StandardOpenOption.CREATE)) {
                for(Class<?> clazz : parsers) {
                    // Cannot instantiate interface and abstract classes
                    if(clazz.isInterface() || Modifier.isAbstract(clazz.getModifiers())) {
                        log.info(clazz.getName() + " is an interface or abstract class, skipping...");
                        continue;
                    }
                    pebbleVars.clear();
                    try {
                        log.trace("Trying to instantiate " + clazz.getName() + "...");
                        MyCsvParser parser = (MyCsvParser)clazz.getDeclaredConstructor().newInstance();
                        /*for(CSVField field : parser.getFields()) {
                            System.out.println(field.name() + ": " + field.description());
                        }*/
                        pebbleVars.put("csvName", parser.getName());
                        pebbleVars.put("fields", parser.getFields());
                        pebbleVars.put("overview", parser.getDescription());
                        template.evaluate(output, pebbleVars); // append to the template
                    } catch(InstantiationException | InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            } catch(Exception e) {
                log.error("Could not generate template!", e);
                System.exit(-2);
            }
        }

        // extend default css for html output
        Attributes attribs = Attributes.builder()
            .linkCss(true) // do not embed style information into the html file
            .copyCss(true) // copy css files next to the generated html files
            .stylesDir(Paths.get("template/css").toAbsolutePath().toString()) // looks for stylesheets in this directory
            .styleSheetName("extendDefault.css") // name of stylesheet file
            .build();
        // TODO: theming for PDF https://docs.asciidoctor.org/pdf-converter/latest/theme/

        // Turn template into pdf
        try(Asciidoctor ascidoc = Asciidoctor.Factory.create()) {
            List.of("pdf", "html5").forEach(backend -> {
                Options pdfOptions = Options.builder()
                    .toDir(outputDir.toFile())
                    .backend(backend)
                    .safe(SafeMode.UNSAFE) // allow embedding files etc
                    .attributes(attribs)
                    .build();
                ascidoc.convertFile(indexAdocFile.toFile(), pdfOptions);
            }); // docbook
        }

        /* Converting all AsciiDoc files in a directory
        Options emptyOptions = Options.builder().build();
        String[] result = asciidoctor.convertDirectory(
            new AsciiDocDirectoryWalker("src/asciidoc"),
            emptyOptions);
        */
    }

    public static PebbleEngine createPebbleEngine() {
        // can change the delimiters like {{ and }} inside the Syntax object in the builder
        return new PebbleEngine.Builder().newLineTrimming(false).build();
    }
}
