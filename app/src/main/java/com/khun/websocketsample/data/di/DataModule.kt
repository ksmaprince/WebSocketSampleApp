package com.khun.websocketsample.data.di

import com.khun.websocketsample.data.repository.TickerRepositoryImpl
import com.khun.websocketsample.domain.repository.TickerRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsCoinbaseRepository(tickerRepositoryImpl: TickerRepositoryImpl): TickerRepository
}