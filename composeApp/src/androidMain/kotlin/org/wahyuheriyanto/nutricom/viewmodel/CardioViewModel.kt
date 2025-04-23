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


class CardioViewModel(context: Context) : ViewModel() {
    private val _prediction = MutableStateFlow<Int?>(null)
    val prediction: StateFlow<Int?> get() = _prediction

    private var interpreter: Interpreter? = null

    init {
        viewModelScope.launch {
            interpreter = Interpreter(loadModelFile(context))
        }
    }

    private suspend fun loadModelFile(context: Context): MappedByteBuffer = withContext(Dispatchers.IO) {
        val fileDescriptor = context.assets.openFd("cardio_model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength)
    }

    private fun normalize(value: Float, min: Float, max: Float): Float {
        return (value - min) / (max - min)
    }
    fun predictCardio(
        age: Int, gender: Int, height: Int, weight: Int,
        apHi: Int, apLo: Int, cholesterol: Int, gluc: Int,
        smoke: Int, alco: Int, active: Int
    ) {
        viewModelScope.launch {
            val input = arrayOf(floatArrayOf(
                //            age, gender, height, weight,
                //            apHi, apLo, cholesterol, gluc, smoke, alco, active
                normalize(age.toFloat(), 30f, 65f),
                gender.toFloat(),
                normalize(height.toFloat(), 145f, 207f),
                normalize(weight.toFloat(), 45f, 120f),
                normalize(apHi.toFloat(), 80f, 160f),
                normalize(apLo.toFloat(), 60f, 120f),
                cholesterol.toFloat(),
                gluc.toFloat(),
                smoke.toFloat(),
                alco.toFloat(),
                active.toFloat()
            ))
            val output = Array(1) { FloatArray(1) }
            Log.e("cekinterpreter"," satu : $interpreter")
            try {
                Log.e("cekinterpreter","input : ${input.size}")
                interpreter?.run(input, output)
                Log.e("cekinterpreter","Output : $output")
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