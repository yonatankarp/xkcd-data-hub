package com.xkcddatahub.adapters.output.http.rest.xkcd.mappers

import com.xkcddatahub.adapters.output.http.rest.xkcd.data.ApiWebComics
import com.xkcddatahub.domain.entity.WebComics

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
