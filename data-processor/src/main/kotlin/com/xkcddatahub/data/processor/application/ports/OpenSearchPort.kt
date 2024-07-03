package com.xkcddatahub.data.processor.application.ports

import com.xkcddatahub.data.processor.domain.entity.DataRecord

interface OpenSearchPort {
    suspend fun indexData(dataRecord: DataRecord)

    suspend fun getComicById(id: Int): DataRecord?

    suspend fun searchComics(keyword: String): List<DataRecord>

    suspend fun getLatestComic(): DataRecord?
}
