package com.xkcddatahub.utilities

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset

object DateTimeUtils {
    fun currentUtc(): LocalDateTime =
        Instant.now()
            .atOffset(ZoneOffset.UTC)
            .toLocalDateTime()
}
