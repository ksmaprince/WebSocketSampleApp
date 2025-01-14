package com.khun.websocketsample.remote.source

import com.khun.websocketsample.data.datasource.remote.TickerRemoteDataSource
import com.khun.websocketsample.data.entity.TickerEntity
import com.khun.websocketsample.remote.mapper.TickerEntityMapper
import com.ashr.cleanMvvmSocket.remote.model.Subscribe
import com.khun.websocketsample.remote.socket.CoinbaseService
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class CoinRemoteDataSourceImpl @Inject constructor(
    private val coinbaseService: CoinbaseService,
    private val tickerEntityMapper: TickerEntityMapper
) : TickerRemoteDataSource {

    override fun observeTicker(): Flow<TickerEntity> {
        return coinbaseService.observeTicker()
            .map { remoteTicker -> tickerEntityMapper.mapFromRemote(remoteTicker) }
    }

    override fun sendSubscribe(subscribe: Subscribe) {
        coinbaseService.sendSubscribe(subscribe)
    }

    override fun observeEvent(): Flow<WebSocket.Event> {
        return coinbaseService.observeWebSocket()
    }
}