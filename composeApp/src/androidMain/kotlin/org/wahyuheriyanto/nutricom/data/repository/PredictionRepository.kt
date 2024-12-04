package org.wahyuheriyanto.nutricom.data.repository

// PredictionRepository.kt
import org.wahyuheriyanto.nutricom.data.InputData
import org.wahyuheriyanto.nutricom.data.PredictionResponse
import org.wahyuheriyanto.nutricom.network.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class PredictionRepository {

    private val retrofit = Retrofit.Builder()
        .baseUrl("https://89bc-103-23-224-196.ngrok-free.app") // Ganti dengan URL Ngrok Anda
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val apiService = retrofit.create(ApiService::class.java)

    suspend fun fetchPrediction(inputData: InputData): PredictionResponse {
        return apiService.getPrediction(inputData) // Pastikan ini sesuai dengan API Anda
    }
}

