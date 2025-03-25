package org.wahyuheriyanto.nutricom.data

import com.google.gson.annotations.SerializedName

data class FoodInfoResponse(
    @SerializedName("product") val product: ProductInfo?
)

data class ProductInfo(
    @SerializedName("product_name") val productName: String?,
    @SerializedName("nutriments") val nutriments: Nutrients?
)

data class Nutrients(
    @SerializedName("energy-kcal_100g") val calories: Double?,
    @SerializedName("proteins_100g") val proteins: Double?,
    @SerializedName("fat_100g") val fat: Double?,
    @SerializedName("carbohydrates_100g") val carbohydrates: Double?
)
