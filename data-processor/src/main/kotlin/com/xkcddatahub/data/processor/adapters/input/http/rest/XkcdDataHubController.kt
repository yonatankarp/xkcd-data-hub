package com.xkcddatahub.data.processor.adapters.input.http.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.xkcddatahub.data.processor.application.usecases.GetComicByIdUseCase
import com.xkcddatahub.data.processor.application.usecases.GetLatestComicUseCase
import com.xkcddatahub.data.processor.application.usecases.SearchComicsUseCase
import io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE
import io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON
import io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR
import io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND
import io.netty.handler.codec.http.HttpResponseStatus.OK
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.ext.web.handler.BodyHandler
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.coroutineRouter

class XkcdDataHubController(
    private val getComicByIdUseCase: GetComicByIdUseCase,
    private val searchComicsUseCase: SearchComicsUseCase,
    private val getLatestComicUseCase: GetLatestComicUseCase,
) : CoroutineVerticle() {
    private val mapper = jacksonObjectMapper()

    override suspend fun start() {
        val router =
            Router.router(vertx).apply {
                route().handler(BodyHandler.create())
            }

        router.getComicById()
        router.search()
        router.getComicsById()

        // TODO: Should be in bootstrap
        vertx.createHttpServer().requestHandler(router).listen(8080)
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

    private fun <RESULT, PARAMS> Router.createRoute(
        url: String,
        parameters: suspend RoutingContext.() -> PARAMS,
        block: suspend (PARAMS) -> RESULT,
    ) = coroutineRouter {
        get(url).coRespond { context ->
            runCatching { block(context.parameters()) }.onFailure {
                it.toInternalErrorResponse(
                    context,
                )
            }.getOrNull()?.toOkResponse(context)
                ?: notFoundResponse(context)
        }
    }

    private fun <T> T.toOkResponse(context: RoutingContext) =
        context.response().setStatusCode(OK.code())
            .putHeader(CONTENT_TYPE, APPLICATION_JSON)
            .end(mapper.writeValueAsString(this))

    private fun notFoundResponse(context: RoutingContext) =
        context.response().setStatusCode(NOT_FOUND.code())
            .putHeader(CONTENT_TYPE, APPLICATION_JSON).end("Comic not found")

    private fun <T : Throwable> T.toInternalErrorResponse(context: RoutingContext) =
        context.response().setStatusCode(INTERNAL_SERVER_ERROR.code())
            .putHeader(CONTENT_TYPE, APPLICATION_JSON)
            .end("Internal server error: $message")
}
