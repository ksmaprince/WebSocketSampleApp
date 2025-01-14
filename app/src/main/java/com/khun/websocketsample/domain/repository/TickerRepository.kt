package com.khun.websocketsample.domain.repository

import com.khun.websocketsample.domain.model.ConnectionState
import kotlinx.coroutines.flow.Flow

interface TickerRepository {

    fun observeEvent(): Flow<ConnectionState>

    fun observeTicker(): Flow<ConnectionState>

    fun subscribeTicker()

}