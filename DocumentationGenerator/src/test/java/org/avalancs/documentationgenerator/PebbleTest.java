package org.avalancs.documentationgenerator;

import io.pebbletemplates.pebble.PebbleEngine;
import io.pebbletemplates.pebble.template.PebbleTemplate;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class PebbleTest {
    static final Path resources = Paths.get("src/test/resources");

    @Test
    public void testPebbleTemplateEngine() throws IOException {
        // can change the delimiters like {{ and }} inside the Syntax object in the builder
        PebbleEngine engine = new PebbleEngine.Builder().newLineTrimming(false).build();

        PebbleTemplate template = engine.getTemplate(resources.resolve("pebbletest.txt").toString());

        Writer writer = new StringWriter();

        Map<String, Object> context = new HashMap<>();
        context.put("testVar", "This is a test variable");
        template.evaluate(writer, context);

        System.out.println(writer.toString());
    }
}
