package org.wahyuheriyanto.nutricom.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.wahyuheriyanto.nutricom.data.TFLiteModel

class HealthViewModel(application: Application) : AndroidViewModel(application) {
    private val tfliteModel = TFLiteModel(application)
    private val _analysisResult = MutableStateFlow("")
    val analysisResult: StateFlow<String> get() = _analysisResult

    // Fungsi untuk normalisasi
    private fun normalize(value: Float, min: Float, max: Float): Float {
        return (value - min) / (max - min)
    }

    fun analyze(gender: Float, age: Float, hypertension: Float, heartDisease: Float, smokingHistory: Float, bmi: Float, hba1c: Float, glucoseLevel: Float) {
        // Normalisasi input sesuai dengan rentang yang sesuai dengan data training
        val inputFeatures = floatArrayOf(
            normalize(gender, 0f, 1f),
            normalize(age, 0f, 100f),
            normalize(hypertension, 0f, 1f),
            normalize(heartDisease, 0f, 1f),
            normalize(smokingHistory, 0f, 4f),
            normalize(bmi, 10f, 50f),
            normalize(hba1c, 3f, 15f),
            normalize(glucoseLevel, 50f, 300f)
        )

        val result = tfliteModel.analyzeInput(inputFeatures)
        _analysisResult.value = if (result == 1) "Diabetes Detected" else "No Diabetes Detected"
    }

    fun resetAnalysis() {
        _analysisResult.value = ""
    }
}



