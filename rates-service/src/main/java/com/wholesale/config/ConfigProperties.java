package com.wholesale.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Setter
@Getter
@ConfigurationProperties(prefix = "exchangerates.api")
public class ConfigProperties {
    private String accessKey;
    private String timeSeriesUrl;
}
