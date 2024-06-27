package com.xkcddatahub.fetcher.adapters.output.http.rest.xkcd

import com.xkcddatahub.fetcher.adapters.output.http.rest.xkcd.data.ApiWebComics
import com.xkcddatahub.fetcher.adapters.output.http.rest.xkcd.mappers.ApiWebComicsMapper.toDomain
import com.xkcddatahub.fetcher.application.ports.XkcdClientPort
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class XkcdClientAdapter(
    private val client: HttpClient,
    private val baseUrl: String,
) : XkcdClientPort {
    override suspend fun getLatestComicId() = client.get("$baseUrl/info.0.json").body<ApiWebComics>().id

    override suspend fun getComicsById(id: Int) =
        client
            .get("$baseUrl/$id/info.0.json")
            .body<ApiWebComics>()
            .toDomain()
}
