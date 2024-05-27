package com.xkcddatahub.webcomics

import com.xkcddatahub.Database.dbQuery
import org.jetbrains.exposed.sql.insert

class ComicRepository {
    suspend fun insertComics(comic: WebComic) = dbQuery {
        WebComics.insert {
            it[id] = comic.id
            it[year] = comic.year
            it[month] = comic.month
            it[day] = comic.day
            it[title] = comic.title
            it[safeTitle] = comic.safeTitle
            it[transcript] = comic.transcript
            it[alternativeText] = comic.alternativeText
            it[imageUrl] = comic.imageUrl
            it[news] = comic.news.ifBlank { null }
            it[link] = comic.link.ifBlank { null }
        }
    }
}
