package org.wahyuheriyanto.nutricom.model

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

data class RecommenderItem(
    val sentence: String = ""
)