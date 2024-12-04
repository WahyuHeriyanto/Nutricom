package org.wahyuheriyanto.nutricom.network

import org.wahyuheriyanto.nutricom.data.InputData
import org.wahyuheriyanto.nutricom.data.PredictionResponse
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.Call

interface ApiService {
    @POST("/predict")
    suspend fun getPrediction(@Body input: InputData): PredictionResponse
}
