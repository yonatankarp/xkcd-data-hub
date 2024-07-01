package com.xkcddatahub.fetcher.adapters.output.database.postgres.table

import com.xkcddatahub.fetcher.adapters.output.database.postgres.data.DatabaseWebComics
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.json.jsonb

object OutboxEventTable : Table("outbox_event") {
    val id = uuid("id")
    val aggregateType = varchar(name = "aggregatetype", length = 255)
    val aggregateId = varchar(name = "aggregateid", length = 255)
    val payload = jsonb<DatabaseWebComics>("payload", Json.Default)

    override val primaryKey = PrimaryKey(id)
}
