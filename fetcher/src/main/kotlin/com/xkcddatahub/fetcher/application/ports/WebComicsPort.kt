package com.xkcddatahub.fetcher.application.ports

import com.xkcddatahub.fetcher.entity.WebComics

interface WebComicsPort {
    suspend fun save(comics: WebComics): Boolean

    suspend fun getLatestStoredComicId(): Int
}
