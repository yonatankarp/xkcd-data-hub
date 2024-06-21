package com.xkcddatahub.fetcher.adapters.output.http.rest.xkcd.mappers

import com.xkcddatahub.fetcher.adapters.output.http.rest.xkcd.data.ApiWebComics
import com.xkcddatahub.fetcher.domain.entity.WebComics

object ApiWebComicsMapper {
    fun ApiWebComics.toDomain() =
        WebComics(
            id = id,
            year = year.toInt(),
            month = month.toInt(),
            day = day.toInt(),
            title = title,
            safeTitle = safeTitle,
            transcript = transcript,
            alternativeText = alternativeText,
            imageUrl = imageUrl,
            news = news,
            link = link,
        )
}
