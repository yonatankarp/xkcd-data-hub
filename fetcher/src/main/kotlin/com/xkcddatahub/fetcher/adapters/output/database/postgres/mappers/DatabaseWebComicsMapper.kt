package com.xkcddatahub.fetcher.adapters.output.database.postgres.mappers

import com.xkcddatahub.fetcher.adapters.output.database.postgres.data.DatabaseWebComics
import com.xkcddatahub.fetcher.domain.entity.WebComics

object DatabaseWebComicsMapper {
    fun WebComics.toData() =
        DatabaseWebComics(
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
