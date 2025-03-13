enum class SendFormat {
    PLAIN_TEXT,
    JSON_SIMPLE,
    JSON_DETAILED,
    JSON_WITH_DEVICE,
    XML,
    CSV;

    fun formatData(
        value: String,
        timestamp: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            .apply { timeZone = TimeZone.getTimeZone("UTC") }
            .format(Date())
    ): String {
        return when (this) {
            PLAIN_TEXT -> value
            JSON_SIMPLE -> """
                {"value": "$value"}
            """.trimIndent()
            JSON_DETAILED -> """
                {
                    "value": "$value",
                    "timestamp": "$timestamp",
                    "type": "${detectCodeType(value)}"
                }
            """.trimIndent()
            JSON_WITH_DEVICE -> """
                {
                    "value": "$value",
                    "timestamp": "$timestamp",
                    "type": "${detectCodeType(value)}",
                    "device": {
                        "id": "${DeviceUtils.getDeviceId()}",
                        "model": "${Build.MODEL}",
                        "manufacturer": "${Build.MANUFACTURER}",
                        "os": "Android ${Build.VERSION.RELEASE}"
                    }
                }
            """.trimIndent()
            XML -> """
                <?xml version="1.0" encoding="UTF-8"?>
                <scan>
                    <value>$value</value>
                    <timestamp>$timestamp</timestamp>
                    <type>${detectCodeType(value)}</type>
                </scan>
            """.trimIndent()
            CSV -> "$value,$timestamp,${detectCodeType(value)}"
        }
    }

    fun getContentType(): String {
        return when (this) {
            PLAIN_TEXT -> "text/plain"
            JSON_SIMPLE, JSON_DETAILED, JSON_WITH_DEVICE -> "application/json"
            XML -> "application/xml"
            CSV -> "text/csv"
        }
    }

    private fun detectCodeType(value: String): String {
        return when {
            value.matches(Regex("^[0-9]+$")) -> "BARCODE"
            value.startsWith("http") -> "URL"
            else -> "QR_CODE"
        }
    }
}