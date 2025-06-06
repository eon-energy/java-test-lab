package com.technokratos.properties;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "test-container")
@Getter
@RequiredArgsConstructor
public class TestContainerProperties {
    private final String dockerFile;
    private final String script;
}
