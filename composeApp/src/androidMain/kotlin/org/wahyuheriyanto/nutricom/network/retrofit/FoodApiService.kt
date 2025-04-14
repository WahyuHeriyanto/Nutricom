package org.wahyuheriyanto.nutricom.network.retrofit

import org.wahyuheriyanto.nutricom.data.remote.response.FoodInfoResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface FoodApiService {
    @GET("api/v0/product/{barcode}.json")
    suspend fun getFoodInfo(@Path("barcode") barcode: String): Response<FoodInfoResponse>
}
