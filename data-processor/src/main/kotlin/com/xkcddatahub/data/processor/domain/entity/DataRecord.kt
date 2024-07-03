package com.xkcddatahub.data.processor.domain.entity

data class DataRecord(
    val id: Int,
    val year: Int,
    val month: Int,
    val day: Int,
    val title: String,
    val safeTitle: String,
    val transcript: String,
    val alternativeText: String,
    val imageUrl: String,
    val news: String,
    val link: String,
    val createdAt: String,
)
