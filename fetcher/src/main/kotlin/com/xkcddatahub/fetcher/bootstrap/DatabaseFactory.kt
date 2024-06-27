package com.xkcddatahub.fetcher.bootstrap

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.config.ApplicationConfig
import kotlinx.coroutines.Dispatchers
import org.flywaydb.core.Flyway
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

class DatabaseFactory(
    private val url: String,
    private val driver: String,
    private val username: String,
    private val password: String,
) {
    fun init() {
        val connection = hikari()
        Database.connect(connection)
        Flyway
            .configure()
            .dataSource(connection)
            .load()
            .migrate()
    }

    private fun hikari(): HikariDataSource {
        val config =
            HikariConfig().apply {
                jdbcUrl = url
                driverClassName = driver
                username = this@DatabaseFactory.username
                password = this@DatabaseFactory.password
                maximumPoolSize = 3
                isAutoCommit = true
                validate()
            }

        return HikariDataSource(config)
    }

    companion object {
        fun from(config: ApplicationConfig): DatabaseFactory =
            DatabaseFactory(
                url = config.property("ktor.database.url").getString(),
                driver = config.property("ktor.database.driver").getString(),
                username = config.property("ktor.database.user").getString(),
                password = config.property("ktor.database.password").getString(),
            )

        suspend fun <T> transaction(block: suspend () -> T): T =
            newSuspendedTransaction(
                Dispatchers.IO,
            ) { block() }
    }
}
