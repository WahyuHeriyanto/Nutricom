package org.wahyuheriyanto.nutricom.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.io.FileInputStream


class DiabetesViewModel(context: Context) : ViewModel() {
    private val _prediction = MutableStateFlow<Int?>(null)
    val prediction: StateFlow<Int?> get() = _prediction

    private var interpreter: Interpreter? = null

    init {
        viewModelScope.launch {
            interpreter = Interpreter(loadModelFile(context))
        }
    }

    private suspend fun loadModelFile(context: Context): MappedByteBuffer = withContext(Dispatchers.IO) {
        val fileDescriptor = context.assets.openFd("diabetes_model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength)
    }

    private fun normalize(value: Float, min: Float, max: Float): Float {
        return (value - min) / (max - min)
    }
    fun predictDiabetes(
        gender: Int, age: Int, hypertension: Int, heartDisease: Int,
        smokingHistory: Int, bmi: Float, hbA1c: Float, bloodGlucose: Int
    ) {
        viewModelScope.launch {
            val input = arrayOf(floatArrayOf(
                gender.toFloat(),
                normalize(age.toFloat(), 0f, 1f),
                hypertension.toFloat(),
                heartDisease.toFloat(),
                smokingHistory.toFloat(),
                normalize(bmi, 0f, 1f),
                normalize(hbA1c, 0f, 1f),
                normalize(bloodGlucose.toFloat(), 0f, 1f)
            ))
            val output = Array(1) { FloatArray(1) }
            Log.e("cekinterpreter"," satu : $interpreter")
            try {
                Log.e("cekinterpreter","dua : $interpreter")
                interpreter?.run(input, output)
                _prediction.value = if (output[0][0] > 0.5) 1 else 0
            } catch (e: Exception) {
                Log.e("DiabetesViewModel", "Error saat menjalankan model: ${e.localizedMessage}")
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        interpreter?.close()
    }
}
