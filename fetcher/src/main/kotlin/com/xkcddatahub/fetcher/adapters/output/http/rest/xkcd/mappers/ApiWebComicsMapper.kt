package com.xkcddatahub.fetcher.adapters.output.http.rest.xkcd.mappers

import com.xkcddatahub.fetcher.adapters.output.http.rest.xkcd.data.ApiWebComics
import com.xkcddatahub.fetcher.entity.WebComics

object ApiWebComicsMapper {
    fun ApiWebComics.toDomain() =
        WebComics(
            id = id,
            year = year,
            month = month,
            day = day,
            title = title,
            safeTitle = safeTitle,
            transcript = transcript,
            alternativeText = alternativeText,
            imageUrl = imageUrl,
            news = news,
            link = link,
        )
}
