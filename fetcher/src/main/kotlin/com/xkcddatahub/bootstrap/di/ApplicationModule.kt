package com.xkcddatahub.bootstrap.di

import com.xkcddatahub.application.usecases.GetAllXkcdComics
import org.koin.dsl.module

val appModule =
    module {
        single { GetAllXkcdComics(get(), get()) }
    }
