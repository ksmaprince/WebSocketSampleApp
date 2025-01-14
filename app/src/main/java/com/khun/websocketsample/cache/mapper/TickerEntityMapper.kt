package com.khun.websocketsample.cache.mapper

import com.khun.websocketsample.cache.model.CachedTicker
import com.khun.websocketsample.data.entity.TickerEntity
import javax.inject.Inject

class TickerEntityMapper @Inject constructor() : EntityMapper<CachedTicker, TickerEntity> {
    override fun mapFromCached(model: CachedTicker): TickerEntity {
        return TickerEntity(
            productId = model.productId,
            price = model.price,
            openPrice = model.openPrice,
            volume24 = model.volume24,
            volumeMonth = model.volumeMonth,
            high24 = model.high24,
            low24 = model.low24
        )
    }

    override fun mapToCached(model: TickerEntity): CachedTicker {
        return CachedTicker(
            productId = model.productId ?: "",
            price = model.price,
            openPrice = model.openPrice,
            volume24 = model.volume24,
            volumeMonth = model.volumeMonth,
            low24 = model.low24,
            high24 = model.high24
        )
    }
}