package com.xkcddatahub.data.processor.application.usecases

import com.xkcddatahub.data.processor.application.ports.OpenSearchPort
import com.xkcddatahub.data.processor.domain.entity.DataRecord

class SearchComicsUseCase(private val openSearchPort: OpenSearchPort) {
    suspend fun execute(keyword: String): List<DataRecord> = openSearchPort.searchComics(keyword)
}
