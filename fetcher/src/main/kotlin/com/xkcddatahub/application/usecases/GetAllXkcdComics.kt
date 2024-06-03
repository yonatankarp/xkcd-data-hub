package com.xkcddatahub.application.usecases

import com.xkcddatahub.application.ports.WebComicsPort
import com.xkcddatahub.application.ports.XkcdClientPort
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory

class GetAllXkcdComics(
    private val client: XkcdClientPort,
    private val repository: WebComicsPort,
) {
    private val logger = LoggerFactory.getLogger(GetAllXkcdComics::class.java)

    suspend operator fun invoke() =
        ioCoroutinesScope {
            val latestComicId = client.getLatestComicId()
            val latestStoredComicId = repository.getLatestStoredComicId()
            for (id in (latestStoredComicId + 1)..latestComicId) {
                runCatching {
                    logger.info("Fetching comic $id")
                    val comics = client.getComicsById(id)
                    repository.save(comics)
                }
                    .onFailure { exception -> logger.error("Failed to fetch comic $exception") }
            }
        }

    private suspend fun <T> ioCoroutinesScope(block: suspend () -> T) =
        coroutineScope {
            withContext(Dispatchers.IO) {
                block()
            }
        }
}
