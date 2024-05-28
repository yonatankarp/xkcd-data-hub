package com.xkcddatahub.webcomics

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class XkcdClient(private val client: HttpClient) {
    suspend fun getMaxId() =
        client.get("https://xkcd.com/info.0.json").body<WebComic>().id

    suspend fun fetchComicsMetadata(id: Int) =
        client.get("https://xkcd.com/$id/info.0.json").body<WebComic>()
}
