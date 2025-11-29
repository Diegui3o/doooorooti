package com.cdp.dotapick.ui.counters

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cdp.dotapick.data.model.Counter
import com.cdp.dotapick.domain.usecase.GetCountersUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CountersViewModel(
    private val getCountersUseCase: GetCountersUseCase
) : ViewModel() {

    private val _countersState = MutableStateFlow<CountersUiState>(CountersUiState.Loading)
    val countersState: StateFlow<CountersUiState> = _countersState

    fun loadCounters(heroId: Int) {
        viewModelScope.launch {
            _countersState.value = CountersUiState.Loading
            try {
                val counters = getCountersUseCase(heroId)
                _countersState.value = CountersUiState.Success(counters.sortedByDescending { it.advantage })
            } catch (e: Exception) {
                _countersState.value = CountersUiState.Error(e.message ?: "Error al cargar counters")
            }
        }
    }
}

sealed class CountersUiState {
    object Loading : CountersUiState()
    data class Success(val counters: List<Counter>) : CountersUiState()
    data class Error(val message: String) : CountersUiState()
}