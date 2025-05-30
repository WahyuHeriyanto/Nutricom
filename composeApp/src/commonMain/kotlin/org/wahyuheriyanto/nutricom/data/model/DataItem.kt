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
    val id: String,
    val imageUrl: String = "",
    val timestamp: Long,
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
    val id: String = "",
    val imageUrl: String = "",
    val barcode: String ="",
    val name: String = "",
    val calories: Long = 0L,
    val cholesterol: Long = 0L,
    val fat:Long = 0L,
    val saturatedFat: Long = 0L,
    val sugars: Long =0L,
    val salt: Long =0L,
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
)

data class UserHealth(
    val activity: Long = 0L,
    val age:Long = 0L,
    val alcoholConsume: Long = 0L,
    val apHi: Long =0L,
    val apLo: Long = 0L,
    val bmi: Long =0L,
    val cardio: Long = 0L,
    val cholesterol: Long = 0L,
    val diabetes:Long = 0L,
    val gluc: Long = 0L,
    val hba1c: Long =0L,
    val hdl: Long = 0L,
    val heartDisease: Long = 0L,
    val height:Long = 0L,
    val ldl:Long = 0L,
    val sleep: Long = 0L,
    val smokingHistory:Long = 0L,
    val tri: Long = 0L,
    val weight: Long =0L,
    val healthComplaint: String = ""
)
