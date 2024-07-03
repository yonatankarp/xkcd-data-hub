package com.xkcddatahub.data.processor.application.usecases

import com.xkcddatahub.data.processor.application.ports.OpenSearchPort
import com.xkcddatahub.data.processor.domain.entity.DataRecord

class IndexDataUseCase(private val openSearchPort: OpenSearchPort) {
    suspend fun execute(dataRecord: DataRecord) = openSearchPort.indexData(dataRecord)
}
