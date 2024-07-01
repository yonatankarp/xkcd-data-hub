package com.xkcddatahub.fetcher.adapters.output.database.postgres

import com.xkcddatahub.fetcher.adapters.output.AbstractIntegrationTest
import com.xkcddatahub.fetcher.adapters.output.database.postgres.data.DatabaseWebComics
import com.xkcddatahub.fetcher.adapters.output.database.postgres.mappers.DatabaseWebComicsMapper.toData
import com.xkcddatahub.fetcher.adapters.output.database.postgres.table.OutboxEventTable
import com.xkcddatahub.fetcher.adapters.output.database.postgres.table.WebComicsTable
import com.xkcddatahub.fetcher.bootstrap.DatabaseFactory.Companion.transaction
import com.xkcddatahub.fetcher.domain.entity.WebComics
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.test.runTest
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class WebComicsPostgresAdapterTest : AbstractIntegrationTest() {
    @BeforeEach
    fun setup() =
        runTest {
            transaction {
                SchemaUtils.create(
                    WebComicsTable,
                    OutboxEventTable,
                )
            }
        }

    @AfterEach
    fun tearDown() =
        runTest {
            transaction {
                SchemaUtils.drop(
                    WebComicsTable,
                    OutboxEventTable,
                )
            }
        }

    @Test
    fun `test save web comic`() =
        runTest {
            // Given
            val webComic = buildWebComics()
            val webComicPostgresAdapter = WebComicPostgresAdapter()

            // When
            val result = webComicPostgresAdapter.save(webComic)

            // Then
            result shouldBe true

            val storedComics =
                transaction { WebComicsTable.selectAll().toList() }
            storedComics.size shouldBe 1

            val storedComic = storedComics.first()
            assertWebComicsAreEqual(webComic, storedComic)

            val outboxEvents =
                transaction { OutboxEventTable.selectAll().toList() }
            outboxEvents.size shouldBe 1

            val event = outboxEvents.first()
            assertOutboxEventsAreEqual(webComic.toData(), event)
        }

    @Test
    fun `test save duplicate web comic`() =
        runTest {
            // Given
            val webComic = buildWebComics(1)
            val webComicPostgresAdapter = WebComicPostgresAdapter()
            webComicPostgresAdapter.save(webComic)

            // When
            val result = webComicPostgresAdapter.save(webComic)

            // Then
            result shouldBe false

            val storedComics =
                transaction { WebComicsTable.selectAll().toList() }
            storedComics.size shouldBe 1

            val outboxEvents =
                transaction { OutboxEventTable.selectAll().toList() }
            outboxEvents.size shouldBe 1
        }

    @Test
    fun `test save web comic with null link and news`() =
        runTest {
            // Given
            val webComic = buildWebComics(id = 1, news = null, link = null)
            val webComicPostgresAdapter = WebComicPostgresAdapter()

            // When
            val result = webComicPostgresAdapter.save(webComic)

            // Then
            result shouldBe true

            val storedComics =
                transaction { WebComicsTable.selectAll().toList() }
            storedComics.size shouldBe 1

            val storedComic = storedComics.first()
            assertWebComicsAreEqual(webComic, storedComic)

            val outboxEvents =
                transaction { OutboxEventTable.selectAll().toList() }
            outboxEvents.size shouldBe 1

            val event = outboxEvents.first()
            assertOutboxEventsAreEqual(webComic.toData(), event)
        }

    @Test
    fun `test concurrency save operations`() =
        runTest {
            // Given
            val webComics = (1..100).map { buildWebComics(it) }
            val webComicPostgresAdapter = WebComicPostgresAdapter()

            // When
            val results =
                webComics.map { comic ->
                    async { webComicPostgresAdapter.save(comic) }
                }.awaitAll()

            // Then
            results.forEach { it shouldBe true }
            val storedComics =
                transaction { WebComicsTable.selectAll().toList() }
            storedComics.size shouldBe 100
        }

    @Test
    fun `test get latest stored comic id`() =
        runTest {
            // Given
            val webComic1 = buildWebComics(1)
            val webComic2 = buildWebComics(2)
            val webComicPostgresAdapter = WebComicPostgresAdapter()

            // When
            webComicPostgresAdapter.save(webComic1)
            webComicPostgresAdapter.save(webComic2)

            // Then
            val latestStoredComicId =
                webComicPostgresAdapter.getLatestStoredComicId()
            latestStoredComicId shouldBe 2
        }

    @Test
    fun `test get latest comic id with no comics`() =
        runTest {
            // Given
            val webComicPostgresAdapter = WebComicPostgresAdapter()

            // When
            val latestStoredComicId =
                webComicPostgresAdapter.getLatestStoredComicId()

            // Then
            latestStoredComicId shouldBe 0
        }

    @Test
    fun `test boundary condition for comic ids`() =
        runTest {
            // Given
            val webComic = buildWebComics(Int.MAX_VALUE)
            val webComicPostgresAdapter = WebComicPostgresAdapter()

            // When
            val result = webComicPostgresAdapter.save(webComic)

            // Then
            result shouldBe true

            val latestStoredComicId =
                webComicPostgresAdapter.getLatestStoredComicId()
            latestStoredComicId shouldBe Int.MAX_VALUE
        }

    private fun buildWebComics(
        id: Int = 1,
        news: String? = "Test news",
        link: String? = "http://testurl.com",
    ) = WebComics(
        id = id,
        year = 2024,
        month = 6,
        day = 24,
        title = "Test Comic",
        safeTitle = "Test Comic Safe",
        transcript = "This is a test transcript",
        alternativeText = "Test alternative text",
        imageUrl = "http://testurl.com/comic.png",
        news = news,
        link = link,
    )

    private fun assertWebComicsAreEqual(
        expected: WebComics,
        actual: ResultRow,
    ) {
        actual[WebComicsTable.id] shouldBe expected.id
        actual[WebComicsTable.year] shouldBe expected.year
        actual[WebComicsTable.month] shouldBe expected.month
        actual[WebComicsTable.day] shouldBe expected.day
        actual[WebComicsTable.title] shouldBe expected.title
        actual[WebComicsTable.safeTitle] shouldBe expected.safeTitle
        actual[WebComicsTable.transcript] shouldBe expected.transcript
        actual[WebComicsTable.alternativeText] shouldBe expected.alternativeText
        actual[WebComicsTable.imageUrl] shouldBe expected.imageUrl
        actual[WebComicsTable.news] shouldBe expected.news
        actual[WebComicsTable.link] shouldBe expected.link
    }

    private fun assertOutboxEventsAreEqual(
        expected: DatabaseWebComics,
        actual: ResultRow,
    ) {
        actual[OutboxEventTable.aggregateType] shouldBe "web_comics_fetched"
        actual[OutboxEventTable.aggregateId] shouldBe expected.id.toString()
        actual[OutboxEventTable.payload] shouldBe expected
    }
}
