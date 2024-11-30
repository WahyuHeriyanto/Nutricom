package org.wahyuheriyanto.nutricom.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.wahyuheriyanto.nutricom.data.HealthPredictionModel


class PredictionViewModel(application: Application) : AndroidViewModel(application) {
    private val predictionModel = HealthPredictionModel(application)
    private val _predictionResult = MutableStateFlow<List<FloatArray>>(emptyList())
    val predictionResult: StateFlow<List<FloatArray>> = _predictionResult

    fun predictTrend(inputData: List<FloatArray>, days: Int) {
        val results = mutableListOf<FloatArray>()
        var currentData = inputData.last()
        repeat(days) {
            val prediction = predictionModel.predict(currentData)
            results.add(prediction[0])
            currentData = prediction[0]
        }
        _predictionResult.value = results
    }
}
