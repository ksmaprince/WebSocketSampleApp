package com.khun.websocketsample.cache.source

import com.khun.websocketsample.cache.db.TickerDao
import com.khun.websocketsample.cache.mapper.TickerEntityMapper
import com.khun.websocketsample.data.datasource.cache.TickerCacheDataSource
import com.khun.websocketsample.data.entity.TickerEntity
import javax.inject.Inject

class TickerCacheDatasourceImpl @Inject constructor(
    private val tickerDao: TickerDao,
    private val tickerEntityMapper: TickerEntityMapper
) : TickerCacheDataSource {

    override suspend fun getAllTicker(): List<TickerEntity> {
        return tickerDao.getAllVehicles()
            .map { cachedVehicle ->
                tickerEntityMapper.mapFromCached(
                    cachedVehicle
                )
            }
    }

    override suspend fun upsertTicker(tickerEntity: TickerEntity) {
        tickerDao.updateVehicle(tickerEntityMapper.mapToCached(tickerEntity))
    }


}