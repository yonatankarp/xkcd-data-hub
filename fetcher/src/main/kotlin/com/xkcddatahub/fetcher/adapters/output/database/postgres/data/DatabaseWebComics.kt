package com.xkcddatahub.fetcher.adapters.output.database.postgres.data

import kotlinx.serialization.Serializable

@Serializable
data class DatabaseWebComics(
    val id: Int,
    val year: String,
    val month: String,
    val day: String,
    val title: String,
    val safeTitle: String,
    val transcript: String,
    val alternativeText: String,
    val imageUrl: String,
    val news: String?,
    val link: String?,
)
