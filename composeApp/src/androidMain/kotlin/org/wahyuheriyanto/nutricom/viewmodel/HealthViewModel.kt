package org.wahyuheriyanto.nutricom.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.wahyuheriyanto.nutricom.data.TFLiteModel

class HealthViewModel(application: Application) : AndroidViewModel(application) {
    private val tfliteModel = TFLiteModel(application)
    private val _analysisResult = MutableStateFlow("")

    // Ubah menjadi StateFlow
    val analysisResult: StateFlow<String> get() = _analysisResult

    fun analyze(age: Float, gender: Float, height: Float, weight: Float, bmi: Float) {
        val inputFeatures = floatArrayOf(age, gender, height, weight, bmi)
        val categoryIndex = tfliteModel.analyzeInput(inputFeatures)
        _analysisResult.value = interpretCategory(categoryIndex)
    }

    private fun interpretCategory(index: Int): String {
        return when (index) {
            0 -> "Normal Weight"
            1 -> "Overweight"
            2 -> "Underweight"
            3 -> "Obese"
            else -> "Unknown"
        }
    }
}

