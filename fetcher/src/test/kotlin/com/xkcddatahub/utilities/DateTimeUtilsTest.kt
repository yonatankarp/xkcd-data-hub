package com.xkcddatahub.utilities

import com.xkcddatahub.utilities.DateTimeUtils.currentUtc
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockkStatic
import io.mockk.unmockkAll
import java.time.Instant
import java.time.LocalDateTime
import java.time.OffsetDateTime
import kotlin.test.AfterTest
import kotlin.test.Test

class DateTimeUtilsTest {

    @AfterTest
    fun tearDown() {
        unmockkAll()
    }

    @Test
    fun `currentUtc should return correct LocalDateTime for given Instant in UTC`() {
        // Given
        val fixedInstant = Instant.parse("2022-01-01T00:00:00Z")
        mockkStatic(Instant::class)
        every { Instant.now() } returns fixedInstant

        // When
        val actualTime = currentUtc()

        // Then
        val expectedTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0)
        actualTime shouldBe expectedTime
    }

    @Test
    fun `currentUtc should return correct LocalDateTime for given Instant in none UTC timezone`() {
        // Given
        val fixedInstant = OffsetDateTime.parse("2022-01-01T03:00:00+03:00").toInstant()
        mockkStatic(Instant::class)
        every { Instant.now() } returns fixedInstant

        // When
        val actualTime = currentUtc()

        // Then
        val expectedTime = LocalDateTime.of(2022, 1, 1, 0, 0, 0)
        actualTime shouldBe expectedTime
    }
}
