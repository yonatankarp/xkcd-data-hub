package com.xkcddatahub.webcomics

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val webComicsModule = module {
    single <HttpClient> {
        HttpClient {
            install(ContentNegotiation) {
                json(Json(Json) { ignoreUnknownKeys = true })
            }
        }
    }
    singleOf(::XkcdClient)
    singleOf(::ComicRepository)
    singleOf(::XkcdService)
}

