package com.xkcddatahub.fetcher.adapters.output.database.postgres.table

import com.xkcddatahub.fetcher.utilities.DateTimeUtils.currentUtc
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object WebComicsTable : Table("web_comics") {
    val id = integer("id")
    val year = integer(name = "year")
    val month = integer(name = "month")
    val day = integer("day")
    val title = text("title")
    val safeTitle = text("safe_title")
    val transcript = text("transcript")
    val alternativeText = text("alternative_text")
    val imageUrl = text("image_url")
    val news = text("news").nullable()
    val link = text("link").nullable()
    val createdAt = datetime("created_at").clientDefault { currentUtc() }

    override val primaryKey = PrimaryKey(id)
}
