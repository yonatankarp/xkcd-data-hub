package com.xkcddatahub.adapters.output.http.rest.xkcd.mappers

import com.xkcddatahub.adapters.output.http.rest.xkcd.data.ApiWebComics
import com.xkcddatahub.adapters.output.http.rest.xkcd.mappers.ApiWebComicsMapper.toDomain
import com.xkcddatahub.domain.entity.WebComics
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ApiWebComicsMapperTest {

    @Test
    fun `test ApiWebComics toDomain mapping`() {
        // Given
        val apiWebComics = apiWebComics(news = "Special news", link = "https://example.com")
        val expectedDomainWebComics = webComics(news = "Special news", link = "https://example.com")

        // When
        val actualDomainWebComics = apiWebComics.toDomain()

        // Then
        actualDomainWebComics shouldBe expectedDomainWebComics
    }

    @Test
    fun `test ApiWebComics toDomain mapping with null news and link`() {
        // Given
        val apiWebComics = apiWebComics()
        val expectedDomainWebComics = webComics()

        // When
        val actualDomainWebComics = apiWebComics.toDomain()

        // Then
        actualDomainWebComics shouldBe expectedDomainWebComics
    }

    private fun apiWebComics(news: String? = null, link: String? = null) =
        ApiWebComics(
            id = 2,
            year = "2022",
            month = "01",
            day = "01",
            title = "New Year Special",
            safeTitle = "New_Year_Special",
            transcript = "A funny comic about New Year.",
            alternativeText = "Alt text for the New Year comic",
            imageUrl = "https://example.com/new_year_special.png",
            news = news,
            link = link
        )

    private fun webComics(news: String? = null, link: String? = null) =
        WebComics(
            id = 2,
            year = "2022",
            month = "01",
            day = "01",
            title = "New Year Special",
            safeTitle = "New_Year_Special",
            transcript = "A funny comic about New Year.",
            alternativeText = "Alt text for the New Year comic",
            imageUrl = "https://example.com/new_year_special.png",
            news = news,
            link = link
        )
}
