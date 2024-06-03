package com.xkcddatahub.adapters.output.http.rest.xkcd.client.mappers

import com.xkcddatahub.adapters.output.http.rest.xkcd.client.data.ApiWebComics
import com.xkcddatahub.domain.entity.WebComics

object WebComicsMapper {
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
            link = link
        )
}
