package com.xkcddatahub.fetcher.output

import com.xkcddatahub.fetcher.bootstrap.DatabaseFactory
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.testcontainers.containers.PostgreSQLContainer
import org.testcontainers.junit.jupiter.Container

abstract class AbstractIntegrationTest {
    companion object {
        @Container
        val postgresContainer: PostgreSQLContainer<*> =
            PostgreSQLContainer<Nothing>("postgres:16")
                .apply {
                    withDatabaseName("xkcd_data_hub")
                    withUsername("test")
                    withPassword("test")
                }
                .also { if (!it.isRunning) it.start() }

        @JvmStatic
        @BeforeAll
        fun initialize() {
            DatabaseFactory(
                url = postgresContainer.jdbcUrl,
                driver = "org.postgresql.Driver",
                username = postgresContainer.username,
                password = postgresContainer.password,
            ).init()
        }

        @JvmStatic
        @AfterAll
        fun finalized() {
            postgresContainer.stop()
        }
    }
}
