package com.xkcddatahub.fetcher.application.ports

import com.xkcddatahub.fetcher.domain.entity.WebComics

interface XkcdClientPort {
    suspend fun getLatestComicId(): Int

    suspend fun getComicsById(id: Int): WebComics
}
