package org.wahyuheriyanto.nutricom.view

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

// Data class untuk rekomendasi
data class RecommendationData(
    val imageUrl: String,
    val sentence: String
)

fun saveRecommendationsIfPredictionIsPositive(prediction: Int?) {
    if (prediction != 1) return

    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val firestore = FirebaseFirestore.getInstance()

    val recommendations = listOf(
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/page1.jpg?alt=media&token=751d6001-a63c-4a92-969f-8cc912e0cfaa",
            sentence = "Pergi ke fasilitas kesehatan terdekat dan berkonsultasi dengan dokter"
        ),
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/page1.jpg?alt=media&token=751d6001-a63c-4a92-969f-8cc912e0cfaa",
            sentence = "Mengurangi konsumsi makanan tinggi gula"
        ),
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/page1.jpg?alt=media&token=751d6001-a63c-4a92-969f-8cc912e0cfaa",
            sentence = "Meningkatkan aktivitas fisik"
        ),
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/page1.jpg?alt=media&token=751d6001-a63c-4a92-969f-8cc912e0cfaa",
            sentence = "Waktu tidur yang berkualitas"
        ),
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/page1.jpg?alt=media&token=751d6001-a63c-4a92-969f-8cc912e0cfaa",
            sentence = "Hindari rokok dan alkohol"
        ),
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/page1.jpg?alt=media&token=751d6001-a63c-4a92-969f-8cc912e0cfaa",
            sentence = "Hindari luka fisik"
        )
    )

    CoroutineScope(Dispatchers.IO).launch {
        recommendations.forEach { recommendation ->
            val docRef = firestore.collection("recommendations")
                .document(userId)
                .collection("active")
                .document()

            val dataMap = mapOf(
                "imageUrl" to recommendation.imageUrl,
                "sentence" to recommendation.sentence,
                "timestamp" to System.currentTimeMillis()
            )

            docRef.set(dataMap)
        }
    }
}