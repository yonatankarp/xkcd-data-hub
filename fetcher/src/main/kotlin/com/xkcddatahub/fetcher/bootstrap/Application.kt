package com.xkcddatahub.fetcher.bootstrap

import com.xkcddatahub.fetcher.application.usecases.GetAllXkcdComics
import com.xkcddatahub.fetcher.bootstrap.di.allModules
import io.ktor.server.application.Application
import io.ktor.server.application.install
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin
import kotlin.time.Duration.Companion.hours

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        modules(allModules)
    }

    DatabaseFactory(environment.config).init()

    fetchXkcdComics()
}

private fun Application.fetchXkcdComics() =
    launch(Dispatchers.IO) {
        val getAllXkcdComics by inject<GetAllXkcdComics>()
        while (true) {
            getAllXkcdComics()
            delay(24.hours.inWholeMilliseconds)
        }
    }
