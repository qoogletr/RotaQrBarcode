package com.rota.RotaQrBarcode.data.repository

import com.rota.RotaQrBarcode.data.local.dao.ScannedCodeDao
import com.rota.RotaQrBarcode.data.local.entity.ScannedCode
import com.rota.RotaQrBarcode.data.remote.api.ApiService
import com.rota.RotaQrBarcode.data.remote.model.BarcodeRequest
import com.rota.RotaQrBarcode.utils.NetworkResult
import com.rota.RotaQrBarcode.utils.PreferencesManager
import kotlinx.coroutines.flow.Flow
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class BarcodeRepository(
    private val dao: ScannedCodeDao,
    private val apiService: ApiService
) {
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    // Local database operations
    fun getAllScannedCodes(): Flow<List<ScannedCode>> = dao.getAllCodes()
    
    fun getPendingCodes(): Flow<List<ScannedCode>> = dao.getPendingCodes()
    
    suspend fun insertScannedCode(code: ScannedCode): Long = dao.insert(code)
    
    suspend fun updateScannedCode(code: ScannedCode) = dao.update(code)
    
    suspend fun deleteScannedCode(code: ScannedCode) = dao.delete(code)
    
    suspend fun clearHistory() = dao.deleteAll()

    // Remote operations
    suspend fun sendToServer(code: ScannedCode): NetworkResult<Boolean> {
        return try {
            val request = BarcodeRequest(
                value = code.value,
                type = code.type,
                format = code.format,
                timestamp = dateFormat.format(code.timestamp)
            )

            val serverUrl = buildServerUrl()
            val response = apiService.sendBarcode(serverUrl, request)

            if (response.isSuccessful && response.body()?.success == true) {
                dao.markAsSent(code.id)
                NetworkResult.Success(true)
            } else {
                NetworkResult.Error(response.body()?.error?.message ?: "Unknown error")
            }
        } catch (e: Exception) {
            NetworkResult.Error(e.message ?: "Network error occurred")
        }
    }

    private fun buildServerUrl(): String {
        val baseUrl = PreferencesManager.serverAddress
        val port = PreferencesManager.serverPort
        val endpoint = PreferencesManager.serverEndpoint.removePrefix("/")
        
        return "http://$baseUrl:$port/$endpoint"
    }

    companion object {
        @Volatile
        private var instance: BarcodeRepository? = null

        fun getInstance(dao: ScannedCodeDao, apiService: ApiService): BarcodeRepository {
            return instance ?: synchronized(this) {
                instance ?: BarcodeRepository(dao, apiService).also { instance = it }
            }
        }
    }
}