package com.xkcddatahub.adapters.output.http.rest.xkcd.client

import com.xkcddatahub.adapters.output.http.rest.xkcd.client.data.ApiWebComics
import com.xkcddatahub.adapters.output.http.rest.xkcd.client.mappers.WebComicsMapper.toDomain
import com.xkcddatahub.application.ports.XkcdClientPort
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get

class XkcdClientAdapter(private val client: HttpClient) : XkcdClientPort {
    override suspend fun getLatestComicId() =
        client.get("https://xkcd.com/info.0.json").body<ApiWebComics>().id

    override suspend fun getComicsById(id: Int) =
        client
            .get("https://xkcd.com/$id/info.0.json")
            .body<ApiWebComics>()
            .toDomain()
}
