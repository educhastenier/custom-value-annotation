package com.bonitasoft.watch.spring.custom_annotation.processor;

import static com.bonitasoft.watch.spring.custom_annotation.processor.PropertyNameExtractor.extractPropertyName;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PropertyNameExtractorTest {

    @Test
    void should_be_able_to_extract_simple_property_name() {
        assertEquals("my.initial.property", extractPropertyName("${my.initial.property}"));
    }

    @Test
    void should_be_able_to_extract_complex_property_name() {
        assertEquals("my.property", extractPropertyName("${my.property:${old.my.property:${older.my.property}}}"));
    }

    @Test
    void should_be_able_to_extract_nested_property_name() {
        assertEquals("server.servlet.session.cookie.name",
                extractPropertyName("#{'${server.servlet.session.cookie.name:${bonita.runtime.cluster.http.session.cookie.name:}}'}"));
    }
}