package org.wahyuheriyanto.nutricom.data

// InputData.kt
data class InputData(
    val date: String,   // Format: 1/1/2024
    val weight: Float,
    val bloodSugar: Float,
    val bloodPressureSystolic: Float,
    val bloodPressureDiastolic: Float,
    val cholesterol: Float,
    val heartRate: Float
)

