package com.xkcddatahub.fetcher.entity

data class WebComics(
    val id: Int,
    val year: String,
    val month: String,
    val day: String,
    val title: String,
    val safeTitle: String,
    val transcript: String,
    val alternativeText: String,
    val imageUrl: String,
    val news: String? = null,
    val link: String? = null,
)
