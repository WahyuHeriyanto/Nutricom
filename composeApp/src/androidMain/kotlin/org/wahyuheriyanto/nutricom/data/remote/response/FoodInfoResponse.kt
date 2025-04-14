package org.wahyuheriyanto.nutricom.data.remote.response

import com.google.gson.annotations.SerializedName

data class FoodInfoResponse(
    @SerializedName("product") val product: ProductInfo?
)

data class ProductInfo(
    @SerializedName("product_name") val productName: String?,
    @SerializedName("nutriments") val nutriments: Nutrients?,
    @SerializedName("code") val code: String?
)

data class Nutrients(
    @SerializedName("energy-kcal_serving") val calories: Double?,
    @SerializedName("sugars_serving") val sugars: Double?,
    @SerializedName("fat_serving") val fat: Double?,
    @SerializedName("salt_serving") val salt: Double?,
    @SerializedName("cholesterol_serving") val cholesterol: Double?,
    @SerializedName("saturated-fat_serving") val saturatedFat: Double?
)
