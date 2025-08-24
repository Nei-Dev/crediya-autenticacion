package com.crediya.r2dbc.config;

import io.r2dbc.pool.ConnectionPool;
import io.r2dbc.pool.ConnectionPoolConfiguration;
import io.r2dbc.spi.ConnectionFactory;
import io.r2dbc.spi.ConnectionFactories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class MySQLConnectionPool {
    public static final int INITIAL_SIZE = 12;
    public static final int MAX_SIZE = 15;
    public static final int MAX_IDLE_TIME = 30;

    @Bean
    public ConnectionPool getConnectionConfig(R2DBCConnectionProperties properties) {
        ConnectionFactory connectionFactory = ConnectionFactories.get(properties.url());

        ConnectionPoolConfiguration poolConfiguration = ConnectionPoolConfiguration.builder(connectionFactory)
                .name("api-mysql-connection-pool")
                .initialSize(INITIAL_SIZE)
                .maxSize(MAX_SIZE)
                .maxIdleTime(Duration.ofMinutes(MAX_IDLE_TIME))
                .validationQuery("SELECT 1")
                .build();

        return new ConnectionPool(poolConfiguration);
    }
}