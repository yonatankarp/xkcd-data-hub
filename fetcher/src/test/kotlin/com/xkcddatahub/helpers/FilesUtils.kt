package com.xkcddatahub.helpers

object FilesUtils {
    fun readFileFromResources(fileName: String) =
        Thread.currentThread()
            .contextClassLoader
            .getResourceAsStream(fileName)
            ?.bufferedReader()
            ?.use { it.readText() }
            ?: throw IllegalArgumentException("File not found: $fileName")
}
