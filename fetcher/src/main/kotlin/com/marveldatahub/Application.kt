package com.marveldatahub

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.server.application.Application
import java.math.BigInteger
import java.security.MessageDigest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val PRIVATE_KEY = "4900722362b19e654e1c13afef9034cfc7f8c7f6"
const val PUBLIC_KEY = "1885c34d3263b37ab5104ee97fd397e3"

fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    launch(Dispatchers.IO) {
        val client = MarvelClient()
        while(true) {
            client.fetchCharacters()
            delay(50_000)
        }
    }
}

class MarvelClient(private val client: HttpClient = HttpClient(CIO) { }) {
    companion object {
        const val BASE_URL = "https://gateway.marvel.com/v1/public"
    }

    suspend fun fetchCharacters() {
        val ts = System.currentTimeMillis()
        val response = client.get("$BASE_URL/characters?ts=${ts}&apikey=${PUBLIC_KEY}&hash=${md5(ts)}")
        println(response.body() as String)
    }
}


fun md5(ts: Long): String {
    val md = MessageDigest.getInstance("MD5")
    return BigInteger(1, md.digest("$ts$PRIVATE_KEY$PUBLIC_KEY".toByteArray())).toString(16)
}
