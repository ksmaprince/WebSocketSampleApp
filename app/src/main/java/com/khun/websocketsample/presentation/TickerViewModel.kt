package com.khun.websocketsample.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.khun.websocketsample.core.di.annotation.HiltDispatchers
import com.khun.websocketsample.domain.model.ConnectionState
import com.khun.websocketsample.domain.model.Ticker
import com.khun.websocketsample.domain.usecase.GetTickersUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TickerViewModel @Inject constructor(
    val getTickersUseCase: GetTickersUseCase,
    @HiltDispatchers.IO private val coroutineDispatcher: CoroutineDispatcher,
) : ViewModel() {

    private val combinedPrices = mutableMapOf<String, Ticker>()

    private val _uiState = MutableStateFlow<TickerUiState>(TickerUiState(data = emptyList()))
    val uiState = _uiState.asStateFlow()

    init {
        println("Init VM")
    }

    fun getCryptos(productId: String? = null) {
        viewModelScope.launch(coroutineDispatcher) {
            getTickersUseCase()
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }
                .onEach { event ->
                    when (event) {
                        is ConnectionState.Connected -> {
                            _uiState.update {
                                it.copy(isLoading = false, isOnline = true)
                            }
                        }

                        is ConnectionState.Success -> {
                            combinedPrices[event.data.productId] = event.data
                            //    Timber.e(combinedPrices.values.toString())
                            _uiState.update { tickerState ->
                                val list = if (productId != null) {
                                    combinedPrices.values.filter { ticker -> ticker.productId == productId }
                                        .toList()
                                } else combinedPrices.values.toList()

                                println(list.toString())
                                tickerState.copy(data = list)
                            }

                        }

                        else -> {
                            _uiState.update {
                                it.copy(isLoading = false, isOnline = false)
                            }
                        }
                    }
                }.collect()
        }
    }

}


data class TickerUiState(
    val data: List<Ticker>,
    val isLoading: Boolean = true,
    val isOnline: Boolean = false,
    val error: String = ""
)
