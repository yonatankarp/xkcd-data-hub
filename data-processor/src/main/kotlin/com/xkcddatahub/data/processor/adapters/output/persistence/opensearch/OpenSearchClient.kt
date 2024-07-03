package com.xkcddatahub.data.processor.adapters.output.persistence.opensearch

import com.xkcddatahub.data.processor.application.ports.OpenSearchPort
import com.xkcddatahub.data.processor.domain.entity.DataRecord
import org.opensearch.action.get.GetRequest
import org.opensearch.action.index.IndexRequest
import org.opensearch.action.search.SearchRequest
import org.opensearch.client.RequestOptions
import org.opensearch.client.RestHighLevelClient
import org.opensearch.common.xcontent.XContentType
import org.opensearch.core.rest.RestStatus
import org.opensearch.index.query.QueryBuilders
import org.opensearch.search.builder.SearchSourceBuilder
import org.opensearch.search.sort.SortOrder

class OpenSearchClient(
    private val client: RestHighLevelClient,
) : OpenSearchPort {
    override suspend fun indexData(dataRecord: DataRecord) {
        val indexRequest =
            IndexRequest("data-records")
                .id(dataRecord.id.toString())
                .source(dataRecord.toMap(), XContentType.JSON)

        val indexResponse = client.index(indexRequest, RequestOptions.DEFAULT)
        if (indexResponse.status() != RestStatus.CREATED) {
            throw RuntimeException("Failed to index data record")
        }
    }

    override suspend fun getComicById(id: Int): DataRecord? {
        val getRequest = GetRequest("data-records", id.toString())
        val getResponse = client.get(getRequest, RequestOptions.DEFAULT)

        return getResponse
            .takeIf { it.isExists }
            ?.sourceAsMap
            ?.toDataRecord()
    }

    override suspend fun searchComics(keyword: String): List<DataRecord> {
        val searchRequest = SearchRequest("data-records")
        val searchSourceBuilder = SearchSourceBuilder()
        searchSourceBuilder.query(
            QueryBuilders.matchQuery(
                "safe_title",
                keyword,
            ),
        )
        searchRequest.source(searchSourceBuilder)

        val searchResponse =
            client.search(searchRequest, RequestOptions.DEFAULT)

        return searchResponse.hits.hits.map { it.sourceAsMap.toDataRecord() }
            .toList()
    }

    override suspend fun getLatestComic(): DataRecord? {
        val searchRequest = SearchRequest("data-records")
        val searchSourceBuilder = SearchSourceBuilder()
        searchSourceBuilder.sort("createdAt", SortOrder.DESC)
        searchSourceBuilder.size(1)
        searchRequest.source(searchSourceBuilder)

        val searchResponse =
            client.search(searchRequest, RequestOptions.DEFAULT)

        return searchResponse.hits.hits
            .firstOrNull()
            ?.sourceAsMap
            ?.toDataRecord()
    }

    private fun Map<String, Any>.toDataRecord() =
        DataRecord(
            id = this["id"] as Int,
            year = this["year"] as Int,
            month = this["month"] as Int,
            day = this["day"] as Int,
            title = this["title"] as String,
            safeTitle = this["safeTitle"] as String,
            transcript = this["transcript"] as String,
            alternativeText = this["alternativeText"] as String,
            imageUrl = this["imageUrl"] as String,
            news = this["news"] as String,
            link = this["link"] as String,
            createdAt = this["createdAt"] as String,
        )

    private fun DataRecord.toMap() =
        mapOf(
            "id" to this.id,
            "year" to this.year,
            "month" to this.month,
            "day" to this.day,
            "title" to this.title,
            "safeTitle" to this.safeTitle,
            "transcript" to this.transcript,
            "alternativeText" to this.alternativeText,
            "imageUrl" to this.imageUrl,
            "news" to this.news,
            "link" to this.link,
            "createdAt" to this.createdAt,
        )
}
