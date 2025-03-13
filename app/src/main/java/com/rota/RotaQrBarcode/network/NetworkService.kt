class NetworkService {
    private val client = OkHttpClient.Builder()
        .connectTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(10, java.util.concurrent.TimeUnit.SECONDS)
        .build()

    suspend fun sendScanResult(
        scannedValue: String,
        format: SendFormat = PreferencesManager.selectedFormat
    ): Result<Boolean> {
        return withContext(Dispatchers.IO) {
            try {
                val serverUrl = buildServerUrl()
                val formattedData = format.formatData(scannedValue)
                
                val request = Request.Builder()
                    .url(serverUrl)
                    .post(
                        RequestBody.create(
                            MediaType.parse(format.getContentType()),
                            formattedData
                        )
                    )
                    .addHeader("Content-Type", format.getContentType())
                    .addHeader("X-Send-Format", format.name)
                    .build()

                val response = client.newCall(request).execute()
                if (response.isSuccessful) {
                    Result.success(true)
                } else {
                    Result.failure(IOException("Server returned ${response.code}"))
                }
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    private fun buildServerUrl(): String {
        val ip = PreferencesManager.serverAddress
        val port = PreferencesManager.serverPort
        val endpoint = PreferencesManager.serverEndpoint
        return "http://$ip:$port$endpoint"
    }
}