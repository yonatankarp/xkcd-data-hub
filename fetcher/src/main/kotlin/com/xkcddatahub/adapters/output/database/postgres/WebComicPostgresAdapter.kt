package com.xkcddatahub.adapters.output.database.postgres

import com.xkcddatahub.adapters.output.database.postgres.mappers.DatabaseWebComicsMapper.toData
import com.xkcddatahub.adapters.output.database.postgres.table.WebComicsTable
import com.xkcddatahub.application.ports.WebComicsPort
import com.xkcddatahub.bootstrap.DatabaseInitializer.Companion.dbQuery
import com.xkcddatahub.domain.entity.WebComics
import org.jetbrains.exposed.sql.insert

class WebComicPostgresAdapter : WebComicsPort {
    override suspend fun save(comics: WebComics): Boolean = dbQuery {
        val data = comics.toData()
        WebComicsTable.insert {
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
        }.insertedCount == 1
    }
}
