package tech.bnpl.apionline.test.utils;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.utility.MountableFile;

public class PostgreSQLContainerSingleton {
    private static final GenericContainer<?> POSTGRESQL_CONTAINER;
    static {
        POSTGRESQL_CONTAINER = new GenericContainer<>("postgres:15")
          .withExposedPorts(5432)
          .withEnv("POSTGRES_USER", "admin")
          .withEnv("POSTGRES_PASSWORD", "admin")
          .withEnv("POSTGRES_DB", "esquema_pagos")
          .withCopyFileToContainer(
            MountableFile.forClasspathResource("schema.sql"),
            "/docker-entrypoint-initdb.d/schema.sql"
          );
        POSTGRESQL_CONTAINER.start();
    }

    public static GenericContainer<?> getPostgreSQLContainer() {
        return POSTGRESQL_CONTAINER;
    }
}
