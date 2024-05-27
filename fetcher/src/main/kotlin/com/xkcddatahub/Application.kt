package com.xkcddatahub

import com.xkcddatahub.DatabaseSingleton.dbQuery
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.Application
import java.time.Instant
import java.time.ZoneOffset
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    DatabaseSingleton.init()
    launch(Dispatchers.IO) {
        val client = XkcdClient()
        coroutineScope {
            repeat(client.getMaxId()) {
                    kotlin.runCatching {
                        println("Fetching comic ${it + 1}")
                        val comics = client.fetchComics(it + 1)
                        Facade().insertComics(comics)
                    }.onFailure { exception -> println("Failed to fetch comic $exception") }
            }
        }
    }
}

class XkcdClient(
    private val client: HttpClient = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json(Json.Default) { ignoreUnknownKeys = true })
        }
    }
) {
    suspend fun getMaxId() = client.get("https://xkcd.com/info.0.json").body<WebComic>().id

    suspend fun fetchComics(id: Int) = client.get("https://xkcd.com/$id/info.0.json").body<WebComic>()
}

@Serializable
data class WebComic(
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
    val news: String, // optional
    val link: String, // optional
)

object WebComics : Table() {
    val id = integer("id")
    val year = text(name = "year")
    val month = text(name = "month")
    val day = text("day")
    val title = text("title")
    val safeTitle = text("safe_title")
    val transcript = text("transcript")
    val alternativeText = text("alternative_text")
    val imageUrl = text("image_url")
    val news = text("news").nullable()
    val link = text("link").nullable()
    val createdAt = datetime("createdAt").clientDefault { currentUtc() }

    override val primaryKey = PrimaryKey(id)
}

private fun currentUtc() = Instant.now().atOffset(ZoneOffset.UTC).toLocalDateTime()


object DatabaseSingleton {
    fun init() {
        val database = Database.connect(
            url = "jdbc:postgresql://localhost:5432/xkcd_data_hub",
            driver = "org.postgresql.Driver",
            user = "postgres",
            password = "secret",
        )

        transaction(database) {
            SchemaUtils.create(WebComics)
        }
    }

    suspend fun <T> dbQuery(block: suspend () -> T): T =
        newSuspendedTransaction(Dispatchers.IO) { block() }
}

class Facade {
    suspend fun insertComics(comic: WebComic) = dbQuery {
        WebComics.insert {
            it[id] = comic.id
            it[year] = comic.year
            it[month] = comic.month
            it[day] = comic.day
            it[title] = comic.title
            it[safeTitle] = comic.safeTitle
            it[transcript] = comic.transcript
            it[alternativeText] = comic.alternativeText
            it[imageUrl] = comic.imageUrl
            it[news] = comic.news.ifBlank { null }
            it[link] = comic.link.ifBlank { null }
        }
    }
}
