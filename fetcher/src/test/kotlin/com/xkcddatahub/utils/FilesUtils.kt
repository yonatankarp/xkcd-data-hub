package com.xkcddatahub.utils

object FilesUtils {
    fun readFileFromResources(fileName: String) =
        Thread.currentThread()
            .contextClassLoader
            .getResourceAsStream(fileName)
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: throw IllegalArgumentException("File not found: $fileName")
}
