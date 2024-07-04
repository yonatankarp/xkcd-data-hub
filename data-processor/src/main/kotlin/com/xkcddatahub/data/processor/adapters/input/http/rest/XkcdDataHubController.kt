package com.xkcddatahub.data.processor.adapters.input.http.rest

import com.xkcddatahub.data.processor.adapters.input.http.rest.mappers.ResponseMapper.notFoundResponse
import com.xkcddatahub.data.processor.adapters.input.http.rest.mappers.ResponseMapper.toInternalErrorResponse
import com.xkcddatahub.data.processor.adapters.input.http.rest.mappers.ResponseMapper.toOkResponse
import com.xkcddatahub.data.processor.application.usecases.GetComicByIdUseCase
import com.xkcddatahub.data.processor.application.usecases.GetLatestComicUseCase
import com.xkcddatahub.data.processor.application.usecases.SearchComicsUseCase
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.coroutineRouter

class XkcdDataHubController(
    private val router: Router,
    private val getComicByIdUseCase: GetComicByIdUseCase,
    private val searchComicsUseCase: SearchComicsUseCase,
    private val getLatestComicUseCase: GetLatestComicUseCase,
) : CoroutineVerticle() {
    override suspend fun start() {
        router.getComicById()
        router.search()
        router.getComicsById()
    }

    private suspend fun Router.getComicById() =
        createRoute(
            url = "/xkcd/webcomics/:comicId",
            parameters = { request().getParam("comicId").toInt() },
            block = { comicId -> getComicByIdUseCase.execute(comicId) },
        )

    private suspend fun Router.search() =
        createRoute(
            url = "/xkcd/webcomics/search",
            parameters = { request().getParam("keyword") },
            block = { keyword -> searchComicsUseCase.execute(keyword) },
        )

    private suspend fun Router.getComicsById() =
        createRoute(
            url = "/xkcd/webcomics",
            parameters = {},
            block = { getLatestComicUseCase.execute() },
        )

    /**
     * A helper function to create a route that handles the request and
     * response.
     * The function will execute the given block and return the
     * result as a JSON response or 404 if the result couldn't be found.
     * If an exception is thrown, the function will return a 500 internal
     * server error.
     *
     * @param url           the url of the route
     * @param parameters    a lambda that extracts the parameters from the
     *                      request using the routing context.
     * @param block         a lambda that handles the request and returns the result
     */
    private fun <RESULT, PARAMS> Router.createRoute(
        url: String,
        parameters: suspend RoutingContext.() -> PARAMS,
        block: suspend (PARAMS) -> RESULT,
    ) = coroutineRouter {
        get(url).coRespond { context ->
            runCatching { block(context.parameters()) }
                .onFailure { it.toInternalErrorResponse(context) }
                .getOrNull()
                ?.toOkResponse(context)
                ?: notFoundResponse(context)
        }
    }
}
