package com.xkcddatahub.fetcher.bootstrap.di

import com.xkcddatahub.fetcher.application.usecases.GetAllXkcdComics
import org.koin.dsl.module

val appModule =
    module {
        single { GetAllXkcdComics(get(), get()) }
    }
