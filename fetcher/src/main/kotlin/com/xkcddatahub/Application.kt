package com.xkcddatahub

import com.xkcddatahub.webcomics.XkcdService
import com.xkcddatahub.webcomics.webComicsModule
import io.ktor.server.application.Application
import io.ktor.server.application.install
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.ktor.ext.inject
import org.koin.ktor.plugin.Koin


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    install(Koin) {
        modules(webComicsModule)
    }
    Database.init()
    launch(Dispatchers.IO) {
        val service by inject<XkcdService>()
        service.fetchAndStoreComics()
    }
}
