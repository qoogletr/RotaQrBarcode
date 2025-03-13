package com.rota.RotaQrBarcode.ui.scanner

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.mlkit.vision.barcode.common.Barcode
import com.rota.RotaQrBarcode.data.local.entity.ScannedCode
import com.rota.RotaQrBarcode.data.repository.BarcodeRepository
import com.rota.RotaQrBarcode.utils.PreferencesManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.*

class ScannerViewModel(
    private val repository: BarcodeRepository
) : ViewModel() {

    private val _scanState = MutableStateFlow<ScanState>(ScanState.Idle)
    val scanState: StateFlow<ScanState> = _scanState

    fun processBarcode(rawValue: String, format: Int, type: Int) {
        viewModelScope.launch {
            try {
                val code = ScannedCode(
                    value = rawValue,
                    format = getBarcodeFormat(format),
                    type = getBarcodeType(type),
                    timestamp = Date().time
                )

                val id = repository.insertScannedCode(code)
                
                if (PreferencesManager.autoSendEnabled) {
                    sendToServer(code.copy(id = id))
                } else {
                    _scanState.value = ScanState.Success(code)
                }
            } catch (e: Exception) {
                _scanState.value = ScanState.Error(e.message ?: "Unknown error occurred")
            }
        }
    }

    private fun sendToServer(code: ScannedCode) {
        viewModelScope.launch {
            _scanState.value = ScanState.Sending
            
            when (val result = repository.sendToServer(code)) {
                is NetworkResult.Success -> {
                    _scanState.value = ScanState.Success(code)
                }
                is NetworkResult.Error -> {
                    _scanState.value = ScanState.Error(result.message)
                }
                is NetworkResult.Loading -> {
                    // Already handling with ScanState.Sending
                }
            }
        }
    }

    private fun getBarcodeFormat(format: Int): String {
        return when (format) {
            Barcode.FORMAT_QR_CODE -> "QR_CODE"
            Barcode.FORMAT_CODE_128 -> "CODE_128"
            Barcode.FORMAT_CODE_39 -> "CODE_39"
            Barcode.FORMAT_EAN_13 -> "EAN_13"
            Barcode.FORMAT_EAN_8 -> "EAN_8"
            Barcode.FORMAT_UPC_A -> "UPC_A"
            Barcode.FORMAT_UPC_E -> "UPC_E"
            else -> "UNKNOWN"
        }
    }

    private fun getBarcodeType(type: Int): String {
        return when (type) {
            Barcode.TYPE_TEXT -> "TEXT"
            Barcode.TYPE_URL -> "URL"
            Barcode.TYPE_PRODUCT -> "PRODUCT"
            Barcode.TYPE_ISBN -> "ISBN"
            Barcode.TYPE_EMAIL -> "EMAIL"
            Barcode.TYPE_PHONE -> "PHONE"
            Barcode.TYPE_SMS -> "SMS"
            Barcode.TYPE_WIFI -> "WIFI"
            else -> "OTHER"
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
                return ScannerViewModel(repository) as T
            }
        }
    }
}

sealed class ScanState {
    object Idle : ScanState()
    object Sending : ScanState()
    data class Success(val code: ScannedCode) : ScanState()
    data class Error(val message: String) : ScanState()
}