data class ScanResult(
    val value: String,
    val type: String,
    val timestamp: String,
    val format: String,
    val deviceInfo: DeviceInfo
)

data class DeviceInfo(
    val deviceId: String,
    val model: String,
    val manufacturer: String,
    val osVersion: String
)