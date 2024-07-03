package com.xkcddatahub.data.processor.bootstrap

import com.xkcddatahub.data.processor.adapters.input.http.rest.XkcdDataHubController
import com.xkcddatahub.data.processor.adapters.output.persistence.opensearch.OpenSearchClient
import com.xkcddatahub.data.processor.application.usecases.GetComicByIdUseCase
import com.xkcddatahub.data.processor.application.usecases.GetLatestComicUseCase
import com.xkcddatahub.data.processor.application.usecases.SearchComicsUseCase
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import org.apache.http.HttpHost
import org.opensearch.client.RestClient
import org.opensearch.client.RestHighLevelClient

class MainVerticle : AbstractVerticle() {
    override fun start(startPromise: Promise<Void>) {
        val openSearchClient = OpenSearchClient(createOpenSearchClient())
        val getComicByIdUseCase = GetComicByIdUseCase(openSearchClient)
        val searchComicsUseCase = SearchComicsUseCase(openSearchClient)
        val getLatestComicUseCase = GetLatestComicUseCase(openSearchClient)

        vertx.deployVerticle(
            XkcdDataHubController(getComicByIdUseCase, searchComicsUseCase, getLatestComicUseCase),
        ) {
            if (it.succeeded()) {
                startPromise.complete()
            } else {
                startPromise.fail(it.cause())
            }
        }
    }

    private fun createOpenSearchClient(): RestHighLevelClient {
        return RestHighLevelClient(
            RestClient.builder(
                HttpHost("localhost", 9200, "http"),
            ),
        )
    }
}
