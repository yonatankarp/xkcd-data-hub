package com.xkcddatahub.data.processor.bootstrap

import com.xkcddatahub.data.processor.adapters.input.http.rest.XkcdDataHubController
import com.xkcddatahub.data.processor.adapters.output.persistence.opensearch.OpenSearchClient
import com.xkcddatahub.data.processor.application.usecases.GetComicByIdUseCase
import com.xkcddatahub.data.processor.application.usecases.GetLatestComicUseCase
import com.xkcddatahub.data.processor.application.usecases.SearchComicsUseCase
import io.vertx.core.AbstractVerticle
import io.vertx.core.Promise
import io.vertx.ext.web.Router
import io.vertx.ext.web.handler.BodyHandler
import org.apache.http.HttpHost
import org.opensearch.client.RestClient
import org.opensearch.client.RestHighLevelClient

class MainVerticle : AbstractVerticle() {
    override fun start(startPromise: Promise<Void>) {
        val openSearchClient = OpenSearchClient(createOpenSearchClient())
        val getComicByIdUseCase = GetComicByIdUseCase(openSearchClient)
        val searchComicsUseCase = SearchComicsUseCase(openSearchClient)
        val getLatestComicUseCase = GetLatestComicUseCase(openSearchClient)

        val router = createRouter()

        val xkcdDataHubController =
            XkcdDataHubController(
                router,
                getComicByIdUseCase,
                searchComicsUseCase,
                getLatestComicUseCase,
            )

        vertx.deployVerticle(xkcdDataHubController) {
            if (it.succeeded()) {
                startPromise.complete()
            } else {
                startPromise.fail(it.cause())
            }
        }

        startHttpServer(router)
    }

    private fun createOpenSearchClient(): RestHighLevelClient {
        return RestHighLevelClient(
            RestClient.builder(
                HttpHost("localhost", 9200, "http"),
            ),
        )
    }

    private fun createRouter() =
        Router.router(vertx).apply {
            route().handler(BodyHandler.create())
        }

    private fun startHttpServer(router: Router) {
        vertx.createHttpServer().requestHandler(router).listen(8080) {
            if (it.succeeded()) {
                println(
                    "HTTP server started on port ${
                        it.result().actualPort()
                    }",
                )
            } else {
                println(
                    "Failed to start HTTP server on port ${
                        it.result().actualPort()
                    }",
                )
            }
        }
    }
}
