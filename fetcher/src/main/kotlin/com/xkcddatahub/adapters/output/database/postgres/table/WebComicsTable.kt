package com.xkcddatahub.adapters.output.database.postgres.table

import com.xkcddatahub.DateTimeUtils.currentUtc
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

object WebComicsTable : Table("web_comics") {
    val id = integer("id")
    val year = text(name = "year")
    val month = text(name = "month")
    val day = text("day")
    val title = text("title")
    val safeTitle = text("safe_title")
    val transcript = text("transcript")
    val alternativeText = text("alternative_text")
    val imageUrl = text("image_url")
    val news = text("news").nullable()
    val link = text("link").nullable()
    val createdAt = datetime("createdAt").clientDefault { currentUtc() }

    override val primaryKey = PrimaryKey(id)
}
