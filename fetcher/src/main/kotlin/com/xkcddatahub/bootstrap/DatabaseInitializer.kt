package com.xkcddatahub.bootstrap

import com.xkcddatahub.adapters.output.database.postgres.table.WebComicsTable
import io.ktor.server.config.ApplicationConfig
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction
import java.sql.DriverManager

class DatabaseInitializer(private val config: ApplicationConfig) {
    fun init() {
        createDatabaseIfNotExists()
        val database =
            Database.connect(
                url = "${
                    config.property("ktor.database.url").getString()
                }${config.property("ktor.database.database").getString()}",
                driver = config.property("ktor.database.driver").getString(),
                user = config.property("ktor.database.user").getString(),
                password = config.property("ktor.database.password").getString(),
            )

        transaction(database) {
            SchemaUtils.create(WebComicsTable)
        }
    }

    @Suppress("SqlSourceToSinkFlow")
    private fun createDatabaseIfNotExists() {
        DriverManager.getConnection(
            config.property("ktor.database.url").getString(),
            config.property("ktor.database.user").getString(),
            config.property("ktor.database.password").getString(),
        ).use { connection ->
            connection.createStatement().use {
                runCatching {
                    val databaseName =
                        config.property("ktor.database.database").getString()
                    it.executeQuery("SELECT 1 FROM pg_database WHERE datname = $databaseName")
                        .takeIf { !it.next() }
                        ?.run { statement.executeUpdate("CREATE DATABASE $databaseName") }
                }
            }
        }
    }

    companion object {
        suspend fun <T> dbQuery(block: suspend () -> T): T = newSuspendedTransaction(Dispatchers.IO) { block() }
    }
}
