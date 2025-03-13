package com.rota.RotaQrBarcode.data.remote.model

import com.google.gson.annotations.SerializedName

data class BarcodeRequest(
    @SerializedName("value")
    val value: String,
    
    @SerializedName("type")
    val type: String,
    
    @SerializedName("format")
    val format: String,
    
    @SerializedName("timestamp")
    val timestamp: String,
    
    @SerializedName("device_info")
    val deviceInfo: DeviceInfo? = null
)

data class DeviceInfo(
    @SerializedName("manufacturer")
    val manufacturer: String,
    
    @SerializedName("model")
    val model: String,
    
    @SerializedName("os_version")
    val osVersion: String,
    
    @SerializedName("app_version")
    val appVersion: String
)