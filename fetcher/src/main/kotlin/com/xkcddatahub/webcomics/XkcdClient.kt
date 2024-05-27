package com.xkcddatahub.webcomics

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

class XkcdClient(
    // TODO: inject properly
    private val client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json(Json.Default) { ignoreUnknownKeys = true })
        }
    }
) {
    suspend fun getMaxId() = client.get("https://xkcd.com/info.0.json").body<WebComic>().id

    suspend fun fetchComicsMetadata(id: Int) = client.get("https://xkcd.com/$id/info.0.json").body<WebComic>()
}
