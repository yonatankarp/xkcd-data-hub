package com.xkcddatahub.application.ports

import com.xkcddatahub.domain.entity.WebComics

interface WebComicsPort {
    suspend fun save(comics: WebComics): Boolean
}
