package com.xkcddatahub.application.ports

import com.xkcddatahub.domain.entity.WebComics

interface XkcdClientPort {
    suspend fun getLatestComicId(): Int

    suspend fun getComicsById(id: Int): WebComics
}
