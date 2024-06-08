package com.xkcddatahub.fetcher.output.http.rest.xkcd

import com.xkcddatahub.fetcher.adapters.output.http.rest.xkcd.XkcdClientAdapter
import com.xkcddatahub.fetcher.adapters.output.http.rest.xkcd.data.ApiWebComics
import com.xkcddatahub.fetcher.adapters.output.http.rest.xkcd.mappers.ApiWebComicsMapper.toDomain
import com.xkcddatahub.helpers.FilesUtils.readFileFromResources
import io.kotest.matchers.shouldBe
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.engine.mock.respondError
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.fullPath
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlin.test.Test

class XkcdClientAdapterTest {
    @Test
    fun `should fetch the latest comic ID successfully`() =
        runTest {
            // Given
            val expectedId = 2940
            val jsonResponse =
                readFileFromResources("responses/xkcd/latest-index.json")
            val client = httpClient(jsonResponse)
            val xkcdClientAdapter = XkcdClientAdapter(client)

            // When
            val actualId = xkcdClientAdapter.getLatestComicId()

            // Then
            actualId shouldBe expectedId
        }

    @Test
    fun `should fetch comic by ID successfully`() =
        runTest {
            // Given
            val comicId = 160
            val webComics = createApiWebComics().toDomain()
            val jsonResponse =
                readFileFromResources("responses/xkcd/webcomics.json")

            val client = httpClient(jsonResponse, comicId)
            val xkcdClientAdapter = XkcdClientAdapter(client)

            // When
            val actualWebComics = xkcdClientAdapter.getComicsById(comicId)

            // Then
            actualWebComics shouldBe webComics
        }

    @Test
    fun `should return nu failing to fetch comic by Id`() =
        runTest {
            // Given
            val comicId = 160
            val webComics = createApiWebComics().toDomain()
            val jsonResponse =
                readFileFromResources("responses/xkcd/webcomics.json")

            val client = httpClient(jsonResponse, comicId)
            val xkcdClientAdapter = XkcdClientAdapter(client)

            // When
            val actualWebComics = xkcdClientAdapter.getComicsById(comicId)

            // Then
            actualWebComics shouldBe webComics
        }

    private fun mockEngine(
        jsonResponse: String,
        comicsId: Int,
    ) = MockEngine { request ->
        when (request.url.fullPath) {
            "/info.0.json", "/$comicsId/info.0.json" ->
                respond(
                    jsonResponse,
                    HttpStatusCode.OK,
                    headersOf("Content-Type" to listOf(ContentType.Application.Json.toString())),
                )

            else -> respondError(HttpStatusCode.NotFound)
        }
    }

    private fun httpClient(
        jsonResponse: String,
        comicsId: Int = 0,
    ) = HttpClient(mockEngine(jsonResponse, comicsId)) {
        install(ContentNegotiation) {
            json()
        }
    }

    private fun createApiWebComics(): ApiWebComics {
        val id = 160
        return ApiWebComics(
            id = 160,
            year = "2023",
            month = "6",
            day = "1",
            title = "Sample Comic $id",
            safeTitle = "Sample_Comic_$id",
            transcript = "This is a sample transcript for comic $id.",
            alternativeText = "Sample alternative text for comic $id.",
            imageUrl = "https://imgs.xkcd.com/comics/penny_arcade_parody.png",
            news = "Sample news for comic $id.",
            link = "https://xkcd.com/$id",
        )
    }
}
