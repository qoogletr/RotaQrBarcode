package com.rota.RotaQrBarcode.data.remote.model

import com.google.gson.annotations.SerializedName

data class BarcodeResponse(
    @SerializedName("success")
    val success: Boolean,
    
    @SerializedName("message")
    val message: String?,
    
    @SerializedName("timestamp")
    val timestamp: String?,
    
    @SerializedName("error")
    val error: ErrorResponse?
)

data class ErrorResponse(
    @SerializedName("code")
    val code: String,
    
    @SerializedName("message")
    val message: String
)