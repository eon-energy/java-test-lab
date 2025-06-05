package com.technokratos.util;

import lombok.experimental.UtilityClass;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

@UtilityClass
public class ContentTypes {
    private static final Map<String, String> CT = Map.of(
            "md",   "text/markdown; charset=" + StandardCharsets.UTF_8.name(),
            "java", "text/x-java-source; charset=" + StandardCharsets.UTF_8.name(),
            "txt",  "text/plain; charset=" + StandardCharsets.UTF_8.name()
    );

    public static String of(String filename) {
        Objects.requireNonNull(filename, "filename");

        String ext = filename.contains(".")
                ? filename.substring(filename.lastIndexOf('.') + 1)
                : filename;

        return CT.getOrDefault(ext.toLowerCase(), "text/plain; charset=utf-8");
    }
}
