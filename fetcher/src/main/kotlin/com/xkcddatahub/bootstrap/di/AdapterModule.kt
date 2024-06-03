package com.xkcddatahub.bootstrap.di

import com.xkcddatahub.adapters.output.database.postgres.WebComicPostgresPort
import com.xkcddatahub.adapters.output.http.rest.xkcd.client.XkcdClientAdapter
import com.xkcddatahub.application.ports.WebComicsPort
import com.xkcddatahub.application.ports.XkcdClientPort
import org.koin.dsl.module

val adapterModule = module {
    single<XkcdClientPort> { XkcdClientAdapter(get()) }
    single<WebComicsPort> { WebComicPostgresPort() }
}
