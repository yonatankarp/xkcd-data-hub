package com.xkcddatahub.data.processor.adapters.input.http.rest.mappers

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE
import io.netty.handler.codec.http.HttpHeaderValues.APPLICATION_JSON
import io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR
import io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND
import io.netty.handler.codec.http.HttpResponseStatus.OK
import io.vertx.core.Future
import io.vertx.ext.web.RoutingContext

object ResponseMapper {
    private val mapper = jacksonObjectMapper()

    fun <T> T.toOkResponse(context: RoutingContext): Future<Void> =
        context.response().setStatusCode(OK.code())
            .putHeader(CONTENT_TYPE, APPLICATION_JSON)
            .end(mapper.writeValueAsString(this))

    fun notFoundResponse(context: RoutingContext): Future<Void> =
        context.response().setStatusCode(NOT_FOUND.code())
            .putHeader(CONTENT_TYPE, APPLICATION_JSON)
            .end("Comic not found")

    fun <T : Throwable> T.toInternalErrorResponse(context: RoutingContext): Future<Void> =
        context.response().setStatusCode(INTERNAL_SERVER_ERROR.code())
            .putHeader(CONTENT_TYPE, APPLICATION_JSON)
            .end("Internal server error: $message")
}
