package com.rota.RotaQrBarcode.data.remote.api

import com.rota.RotaQrBarcode.data.remote.model.BarcodeRequest
import com.rota.RotaQrBarcode.data.remote.model.BarcodeResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Url

interface ApiService {
    @POST
    suspend fun sendBarcode(
        @Url url: String,
        @Body request: BarcodeRequest
    ): Response<BarcodeResponse>
}