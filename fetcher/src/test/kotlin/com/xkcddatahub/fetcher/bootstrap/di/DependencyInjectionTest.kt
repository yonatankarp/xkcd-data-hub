package com.xkcddatahub.fetcher.bootstrap.di

import org.junit.jupiter.api.Test
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class DependencyInjectionTest : KoinTest {
    @Test
    fun `dependency injection context is loaded correctly`() {
        checkModules {
            modules(
                networkModule,
                adapterModule(xkcdBaseUrl = "https://xkcd.com"),
                appModule,
            )
        }
    }
}
