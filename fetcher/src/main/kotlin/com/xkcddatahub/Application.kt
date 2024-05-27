package com.xkcddatahub

import com.xkcddatahub.webcomics.XkcdService
import io.ktor.server.application.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    Database.init()
    launch(Dispatchers.IO) {
        XkcdService().fetchAndStoreComics()
    }
}
