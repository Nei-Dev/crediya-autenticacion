package com.crediya.r2dbc.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "adapters.r2dbc")
public record R2DBCConnectionProperties(String url) {
}
