package com.xkcddatahub.webcomics

import kotlinx.coroutines.coroutineScope

class XkcdService(
    private val client: XkcdClient,
    private val repository: ComicRepository,
) {
    suspend fun fetchAndStoreComics() = coroutineScope {
        repeat(client.getMaxId()) {
            kotlin.runCatching {
                println("Fetching comic ${it + 1}")
                val comics = client.fetchComicsMetadata(it + 1)
                repository.insertComics(comics)
            }.onFailure { exception -> println("Failed to fetch comic $exception") }
        }
    }
}
