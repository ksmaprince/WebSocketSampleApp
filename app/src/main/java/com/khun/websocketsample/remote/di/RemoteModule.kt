package com.khun.websocketsample.remote.di

import android.content.Context
import com.khun.websocketsample.WebSocketSampleApp
import com.khun.websocketsample.data.datasource.remote.TickerRemoteDataSource
import com.khun.websocketsample.remote.mapper.TickerEntityMapper
import com.khun.websocketsample.remote.socket.CoinbaseService
import com.khun.websocketsample.remote.socket.FlowStreamAdapter
import com.khun.websocketsample.remote.source.CoinRemoteDataSourceImpl
import com.squareup.moshi.Moshi
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.BuildConfig
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    @Provides
    @Singleton
    fun provideScarlet( client: OkHttpClient, moshi: Moshi): Scarlet {
        return Scarlet.Builder()
            .webSocketFactory(client.newWebSocketFactory(BASE_URL))
            .addMessageAdapterFactory(MoshiMessageAdapter.Factory(moshi))
            .addStreamAdapterFactory(FlowStreamAdapter.Factory())
            //.lifecycle(AndroidLifecycle.ofApplicationForeground(application))
            //.backoffStrategy(LinearBackoffStrategy(1000L))
            .build()
    }

    @Provides
    @Singleton
    fun providesApplication(@ApplicationContext context: Context): WebSocketSampleApp {
        return context as WebSocketSampleApp
    }


    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .build()
    }


    @Provides
    @Singleton
    fun provideHttpClient(): OkHttpClient {
        val logger = HttpLoggingInterceptor()
            .setLevel(
                if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BASIC
                else HttpLoggingInterceptor.Level.NONE
            )

        return OkHttpClient.Builder()
            .addInterceptor(logger)
            .build()
    }

    @Provides
    @Singleton
    fun provideCoinbaseService(scarlet: Scarlet): CoinbaseService {
        return scarlet.create()
    }

    @Provides
    @Singleton
    fun provideCoinRemoteDataSource(
        coinbaseService: CoinbaseService,
        tickerEntityMapper: TickerEntityMapper
    ): TickerRemoteDataSource =
        CoinRemoteDataSourceImpl(coinbaseService, tickerEntityMapper)

    companion object {
        const val BASE_URL = "wss://ws-feed.pro.coinbase.com/"
    }
}