package com.xkcddatahub.application.usecases

import com.xkcddatahub.application.ports.WebComicsPort
import com.xkcddatahub.application.ports.XkcdClientPort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext

class GetAllXkcdComics(
    private val client: XkcdClientPort,
    private val repository: WebComicsPort,
) {
    suspend operator fun invoke() =
        ioCoroutinesScope {
            val latestComicId = client.getLatestComicId()
            for (id in 1..latestComicId) {
                runCatching {
                    println("Fetching comic $id")
                    val comics = client.getComicsById(id)
                    repository.save(comics)
                }
                    .onFailure { exception -> println("Failed to fetch comic $exception") }
            }
        }

    private suspend fun <T> ioCoroutinesScope(block: suspend () -> T) =
        coroutineScope {
            withContext(Dispatchers.IO) {
                block()
            }
        }
}
