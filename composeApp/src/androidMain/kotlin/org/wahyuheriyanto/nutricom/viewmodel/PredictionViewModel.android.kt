package org.wahyuheriyanto.nutricom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.wahyuheriyanto.nutricom.data.InputData
import org.wahyuheriyanto.nutricom.data.repository.PredictionRepository

// PredictionViewModel.kt
class PredictionViewModel : ViewModel() {
    private val repository = PredictionRepository()

    private val _predictionResult = MutableStateFlow<List<Float>?>(null)
    val predictionResult: StateFlow<List<Float>?> = _predictionResult

    fun getPrediction(inputData: InputData) {
        viewModelScope.launch {
            try {
                val response = repository.fetchPrediction(inputData)
                _predictionResult.value = response.prediction
            } catch (e: Exception) {
                _predictionResult.value = null
                e.printStackTrace()
            }
        }
    }
}


