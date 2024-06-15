package com.xkcddatahub.fetcher.output.database.postgres.mappers

import com.xkcddatahub.fetcher.adapters.output.database.postgres.data.DatabaseWebComics
import com.xkcddatahub.fetcher.adapters.output.database.postgres.mappers.DatabaseWebComicsMapper.toData
import com.xkcddatahub.fetcher.entity.WebComics
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class DatabaseWebComicsMapperTest {
    @Test
    fun `test WebComics toData mapping`() {
        // Given
        val webComics = webComics(news = "Special news", link = "https://example.com")
        val expectedDatabaseWebComics = databaseWebComics(news = "Special news", link = "https://example.com")

        // When
        val actualDatabaseWebComics = webComics.toData()

        // Then
        actualDatabaseWebComics shouldBe expectedDatabaseWebComics
    }

    @Test
    fun `test WebComics toData mapping with null news and link`() {
        // Given
        val webComics = webComics()
        val expectedDatabaseWebComics = databaseWebComics()

        // When
        val actualDatabaseWebComics = webComics.toData()

        // Then
        actualDatabaseWebComics shouldBe expectedDatabaseWebComics
    }

    private fun webComics(
        news: String? = null,
        link: String? = null,
    ) = WebComics(
        id = 1,
        year = 2021,
        month = 12,
        day = 25,
        title = "Christmas Special",
        safeTitle = "Christmas_Special",
        transcript = "A funny comic about Christmas.",
        alternativeText = "Alt text for the comic",
        imageUrl = "https://example.com/christmas_special.png",
        news = news,
        link = link,
    )

    private fun databaseWebComics(
        news: String? = null,
        link: String? = null,
    ) = DatabaseWebComics(
        id = 1,
        year = 2021,
        month = 12,
        day = 25,
        title = "Christmas Special",
        safeTitle = "Christmas_Special",
        transcript = "A funny comic about Christmas.",
        alternativeText = "Alt text for the comic",
        imageUrl = "https://example.com/christmas_special.png",
        news = news,
        link = link,
    )
}
