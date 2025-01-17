package com.khun.websocketsample.remote.mapper

import com.khun.websocketsample.data.entity.TickerEntity
import com.khun.websocketsample.remote.model.RemoteTicker
import javax.inject.Inject

class TickerEntityMapper @Inject constructor() : EntityMapper<RemoteTicker, TickerEntity> {

    override fun mapFromRemote(type: RemoteTicker): TickerEntity {
        return TickerEntity(
            productId = type.productId,
            price = type.price,
            openPrice = type.openPrice,
            volume24 = type.volume24,
            volumeMonth = type.volume30d,
            low24 = type.low24,
            high24 = type.high24
        )
    }
}