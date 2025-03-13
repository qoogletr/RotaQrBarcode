import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.barcodescanner.network.NetworkService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ScannerViewModel : ViewModel() {
    private val networkService = NetworkService()
    private val _sendState = MutableStateFlow<SendState>(SendState.Idle)
    val sendState: StateFlow<SendState> = _sendState

    fun sendToServer(scannedValue: String) {
        viewModelScope.launch {
            _sendState.value = SendState.Sending

            try {
                val result = networkService.sendScanResult(scannedValue)
                result.fold(
                    onSuccess = {
                        _sendState.value = SendState.Success
                    },
                    onFailure = { exception ->
                        _sendState.value = SendState.Error(exception.message ?: "Unknown error")
                    }
                )
            } catch (e: Exception) {
                _sendState.value = SendState.Error(e.message ?: "Unknown error")
            }
        }
    }
}

sealed class SendState {
    object Idle : SendState()
    object Sending : SendState()
    object Success : SendState()
    data class Error(val message: String) : SendState()
}