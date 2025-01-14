package com.khun.websocketsample.data.datasource.cache

import com.khun.websocketsample.data.entity.TickerEntity

interface TickerCacheDataSource {

   suspend fun getAllTicker():List<TickerEntity>

    suspend fun upsertTicker(tickerEntity: TickerEntity)
}