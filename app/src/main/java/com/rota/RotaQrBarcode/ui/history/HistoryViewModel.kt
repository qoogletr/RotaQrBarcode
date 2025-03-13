package com.rota.RotaQrBarcode.ui.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.rota.RotaQrBarcode.RotaBarcodeApp
import com.rota.RotaQrBarcode.data.local.database.AppDatabase
import com.rota.RotaQrBarcode.data.local.entity.ScannedCode
import com.rota.RotaQrBarcode.data.repository.BarcodeRepository
import com.rota.RotaQrBarcode.network.ApiClient
import com.rota.RotaQrBarcode.utils.NetworkResult
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*

class HistoryViewModel(
    private val repository: BarcodeRepository
) : ViewModel() {

    private val _historyState = MutableStateFlow<HistoryState>(HistoryState.Loading)
    val historyState: StateFlow<HistoryState> = _historyState

    private val _statistics = MutableStateFlow(Statistics())
    val statistics: StateFlow<Statistics> = _statistics

    init {
        loadHistory()
        loadStatistics()
    }

    private fun loadHistory() {
        viewModelScope.launch {
            try {
                repository.getAllScannedCodes()
                    .collect { codes ->
                        _historyState.value = HistoryState.Success(codes)
                    }
            } catch (e: Exception) {
                _historyState.value = HistoryState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun loadStatistics() {
        viewModelScope.launch {
            combine(
                repository.getCount(),
                repository.getSentCount()
            ) { total, sent ->
                Statistics(
                    totalScans = total,
                    sentScans = sent,
                    pendingScans = total - sent
                )
            }.collect {
                _statistics.value = it
            }
        }
    }

    fun syncPendingCodes() {
        viewModelScope.launch {
            repository.getPendingCodes().collect { pendingCodes ->
                pendingCodes.forEach { code ->
                    when (repository.sendToServer(code)) {
                        is NetworkResult.Success -> {
                            // Update UI if needed
                        }
                        is NetworkResult.Error -> {
                            // Handle error
                        }
                        is NetworkResult.Loading -> {
                            // Show loading if needed
                        }
                    }
                }
            }
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            try {
                repository.clearHistory()
            } catch (e: Exception) {
                _historyState.value = HistoryState.Error(e.message ?: "Failed to clear history")
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val database = AppDatabase.getInstance(RotaBarcodeApp.instance)
                val apiService = ApiClient.create()
                val repository = BarcodeRepository.getInstance(
                    database.scannedCodeDao(),
                    apiService
                )
                return HistoryViewModel(repository) as T
            }
        }
    }
}

sealed class HistoryState {
    object Loading : HistoryState()
    data class Success(val codes: List<ScannedCode>) : HistoryState()
    data class Error(val message: String) : HistoryState()
}

data class Statistics(
    val totalScans: Int = 0,
    val sentScans: Int = 0,
    val pendingScans: Int = 0
)