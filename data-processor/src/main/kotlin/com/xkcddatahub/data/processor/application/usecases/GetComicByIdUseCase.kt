package com.xkcddatahub.data.processor.application.usecases

import com.xkcddatahub.data.processor.application.ports.OpenSearchPort
import com.xkcddatahub.data.processor.domain.entity.DataRecord

class GetComicByIdUseCase(private val openSearchPort: OpenSearchPort) {
    suspend fun execute(id: Int): DataRecord? = openSearchPort.getComicById(id)
}
