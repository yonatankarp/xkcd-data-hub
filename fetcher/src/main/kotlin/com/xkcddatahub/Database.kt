package com.xkcddatahub

import com.xkcddatahub.webcomics.WebComics
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object Database {
    fun init() {
        val database = Database.connect(
            url = "jdbc:postgresql://localhost:5432/xkcd_data_hub",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "secret",
        )

        transaction(database) {
            SchemaUtils.create(WebComics)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}
