package com.xkcddatahub.fetcher.bootstrap.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import org.junit.jupiter.api.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.verify.Verify.verify

@OptIn(KoinExperimentalAPI::class)
class DependencyInjectionTest : KoinTest {
    @Test
    fun `dependency injection context is loaded correctly`() {
        val allModules =
            module {
                includes(
                    networkModule,
                    adapterModule(xkcdBaseUrl = "https://xkcd.com"),
                    appModule,
                )
            }

        val testModule =
            module {
                single<HttpClientEngine> {
                    MockEngine { respond("ok", HttpStatusCode.OK) }
                }
            }

        verify(module = module { includes(allModules, testModule) })
    }
}
