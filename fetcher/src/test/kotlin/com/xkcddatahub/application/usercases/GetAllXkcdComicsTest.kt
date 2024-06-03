package com.xkcddatahub.application.usercases

import com.xkcddatahub.application.ports.WebComicsPort
import com.xkcddatahub.application.ports.XkcdClientPort
import com.xkcddatahub.application.usecases.GetAllXkcdComics
import com.xkcddatahub.domain.entity.WebComics
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.BeforeTest
import kotlin.test.Test

class GetAllXkcdComicsTest {
    private val client = mockk<XkcdClientPort>()
    private val repository = mockk<WebComicsPort>()

    @BeforeTest
    fun setUp() {
        clearAllMocks()
    }

    @Test
    fun `should fetch and save all comics successfully`() =
        runTest {
            // Given
            val getAllXkcdComics = GetAllXkcdComics(client, repository)

            val latestComicId = 3
            val comics = createTestWebComics()

            coEvery { client.getLatestComicId() } returns latestComicId
            coEvery { repository.getLatestStoredComicId() } returns 0
            coEvery { client.getComicsById(any()) } returns comics
            coEvery { repository.save(any()) } returns true

            // When
            getAllXkcdComics()

            // Then
            coVerify(exactly = latestComicId) { client.getComicsById(any()) }
            coVerify(exactly = latestComicId) { repository.save(comics) }
        }

    @Test
    fun `should handle exceptions during comic fetch`() =
        runTest {
            // Given
            val getAllXkcdComics = GetAllXkcdComics(client, repository)

            val latestComicId = 3
            val comics = createTestWebComics()

            coEvery { client.getLatestComicId() } returns latestComicId
            coEvery { repository.getLatestStoredComicId() } returns 0
            coEvery { client.getComicsById(1) } returns comics
            coEvery { client.getComicsById(2) } throws Exception("Failed to fetch comic")
            coEvery { client.getComicsById(3) } returns comics
            coEvery { repository.save(any()) } returns true

            // When
            getAllXkcdComics()

            // Then
            coVerify(exactly = 3) { client.getComicsById(any()) }
            coVerify(exactly = 2) { repository.save(comics) }
        }

    @Test
    fun `should not fetch data when latest stored id is the same as latest available id`() =
        runTest {
            // Given
            val getAllXkcdComics = GetAllXkcdComics(client, repository)

            val latestComicId = 5
            val comics = createTestWebComics()

            coEvery { client.getLatestComicId() } returns latestComicId
            coEvery { repository.getLatestStoredComicId() } returns latestComicId

            // When
            getAllXkcdComics()

            // Then
            coVerify(exactly = 0) { client.getComicsById(any()) }
            coVerify(exactly = 0) { repository.save(comics) }
        }

    private fun createTestWebComics(id: Int = 1) =
        WebComics(
            id = id,
            year = "2023",
            month = "06",
            day = "01",
            title = "Sample Comic $id",
            safeTitle = "Sample_Comic_$id",
            transcript = "This is a sample transcript for comic $id.",
            alternativeText = "Sample alternative text for comic $id.",
            imageUrl = "https://xkcd.com/$id/info.0.json",
            news = "Sample news for comic $id.",
            link = "https://xkcd.com/$id",
        )
}
