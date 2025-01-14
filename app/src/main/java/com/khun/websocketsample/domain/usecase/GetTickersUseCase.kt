package com.khun.websocketsample.domain.usecase

import com.khun.websocketsample.domain.repository.TickerRepository
import kotlinx.coroutines.flow.merge
import javax.inject.Inject

class GetTickersUseCase @Inject constructor(private val coinbaseRepository: TickerRepository) {
    operator fun invoke() =
        merge(coinbaseRepository.observeTicker(), coinbaseRepository.observeEvent())
}