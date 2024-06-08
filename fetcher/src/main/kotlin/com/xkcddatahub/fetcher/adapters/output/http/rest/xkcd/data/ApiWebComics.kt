package com.xkcddatahub.fetcher.adapters.output.http.rest.xkcd.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("web_comic")
data class ApiWebComics(
    @SerialName("num")
    val id: Int,
    val year: String,
    val month: String,
    val day: String,
    val title: String,
    @SerialName("safe_title")
    val safeTitle: String,
    val transcript: String,
    @SerialName("alt")
    val alternativeText: String,
    @SerialName("img")
    val imageUrl: String,
    val news: String?,
    val link: String?,
)
