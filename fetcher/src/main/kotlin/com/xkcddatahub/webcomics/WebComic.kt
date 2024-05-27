package com.xkcddatahub.webcomics

import com.xkcddatahub.DateTimeUtils.currentUtc
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.javatime.datetime

@Serializable
data class WebComic(
    @SerialName("num")
    val id: Int,
    val year: String,
    val month: String,
    val day: String,
    val title: String,
    @SerialName("safe_title")
    val safeTitle: String,
    val transcript: String,
    @SerialName("alt")
    val alternativeText: String,
    @SerialName("img")
    val imageUrl: String,
    val news: String, // optional
    val link: String, // optional
)

object WebComics : Table() {
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
