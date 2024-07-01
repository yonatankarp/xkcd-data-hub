package com.xkcddatahub.fetcher.adapters.output.database.postgres

import com.xkcddatahub.fetcher.adapters.output.database.postgres.data.DatabaseWebComics
import com.xkcddatahub.fetcher.adapters.output.database.postgres.mappers.DatabaseWebComicsMapper.toData
import com.xkcddatahub.fetcher.adapters.output.database.postgres.table.OutboxEventTable
import com.xkcddatahub.fetcher.adapters.output.database.postgres.table.WebComicsTable
import com.xkcddatahub.fetcher.application.ports.WebComicsPort
import com.xkcddatahub.fetcher.bootstrap.DatabaseFactory.Companion.transaction
import com.xkcddatahub.fetcher.domain.entity.WebComics
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.max
import java.util.UUID

class WebComicPostgresAdapter : WebComicsPort {
    override suspend fun save(comics: WebComics): Boolean =
        transaction {
            val data = comics.toData()
            WebComicsTable.insertIgnore {
                it[id] = data.id
                it[year] = data.year
                it[month] = data.month
                it[day] = data.day
                it[title] = data.title
                it[safeTitle] = data.safeTitle
                it[transcript] = data.transcript
                it[alternativeText] = data.alternativeText
                it[imageUrl] = data.imageUrl
                it[news] = data.news
                it[link] = data.link
            }
                .takeIf { it.insertedCount == 1 }
                ?.let { outbox(data) }
                ?: false
        }

    private fun outbox(comics: DatabaseWebComics): Boolean =
        OutboxEventTable.insert {
            it[id] = UUID.randomUUID()
            it[aggregateType] = AGGREGATE_TYPE
            it[aggregateId] = comics.id.toString()
            it[payload] = comics
        }.insertedCount == 1

    override suspend fun getLatestStoredComicId(): Int =
        transaction {
            WebComicsTable
                .select(WebComicsTable.id.max())
                .single()[WebComicsTable.id.max()]
                ?: 0
        }

    companion object {
        private const val AGGREGATE_TYPE = "web_comics_fetched"
    }
}
