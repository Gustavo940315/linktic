package com.linktic.ms.products.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Data
@Component
@ConfigurationProperties(prefix = "handle")
public class ExceptionConfigs {

    private Map<String, String> exception;

    public static final String BUSINESS = "business";
    public static final String SYSYEM = "system";

    public String getException(String key) {
        return exception.getOrDefault(key, "Error desconocido");
    }
}
