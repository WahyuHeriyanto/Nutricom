package org.wahyuheriyanto.nutricom.data.model

data class DataItem(
    val uid:String = "",
    val weight: Int = 0,
    val height: Int = 0,
    val calories: Int = 0,
    val bmi: Int =0

)
data class UidItem(
    var uid:String = ""
)

data class Article(
    val title: String = "",
    val author: String = "",
    val content: String = "",
    val imageUrl: String = "",
    val timestamp: Long = 0L
)

data class ScreeningItem(
    val imageUrl: String = "",
    val date: String = "",
    val type: String = ""
)

data class Nutricions(
    val calories: Long = 0L,
    val proteins: Long = 0L,
    val fat: Long = 0L,
    val carbo: Long = 0L,
    val saturatedFat:Long = 0L,
    val salt:Long = 0L,
    val cholesterol:Long = 0L
)

data class RecommenderItem(
    val id: String = "",
    val imageUrl: String ="",
    val sentence: String = ""
)

data class ConsumtionItem(
    val imageUrl: String = "",
    val barcode: String ="",
    val name: String = "",
    val calories: Long = 0L,
    val cholesterol: Long = 0L,
    val fat:Long = 0L,
    val saturatedFat: Long = 0L,
    val sugars: Long =0L,
    val timestamp: Long = 0L
)

data class UserProfile(
    val imageUrl: String = "",
    val fullName: String = "",
    val userName: String = "",
    val gender: String = "",
    val email: String = "",
    val dateOfBirth: String = "",
    val phoneNumber: String = "",
    val age: String = "",
    val weight: String = "",
    val height: String = "",
    val smokingHistory: String = "",
    val alcoholConsume: String = ""
)
