package com.bonitasoft.watch.spring.custom_annotation.processor;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Extracts the first property name from the form:
 * Eg. "${my.property:${old.my.property:${older.my.property}}}" returns "my.property"
 * Eg. #{'${server.servlet.session.cookie.name:${bonita.runtime.cluster.http.session.cookie.name:}}'} returns "server.servlet.session.cookie.name"
 */
public class PropertyNameExtractor {
    private static final Pattern PATTERN = Pattern.compile("^(\\$\\{|#\\{'\\$\\{)*([^:}]+)[:}]?.*$");

    public static String extractPropertyName(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }

        Matcher matcher = PATTERN.matcher(value);
        if (matcher.find()) {
            if (matcher.group(2) != null) {
                return matcher.group(2);
            }
        }

        return value;
    }

}