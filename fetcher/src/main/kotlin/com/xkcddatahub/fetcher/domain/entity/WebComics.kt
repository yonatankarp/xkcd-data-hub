package com.xkcddatahub.fetcher.domain.entity

data class WebComics(
    val id: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val title: String,
    val safeTitle: String,
    val transcript: String,
    val alternativeText: String,
    val imageUrl: String,
    val news: String? = null,
    val link: String? = null,
)
