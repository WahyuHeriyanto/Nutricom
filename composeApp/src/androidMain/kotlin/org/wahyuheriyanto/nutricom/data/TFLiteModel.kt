package org.wahyuheriyanto.nutricom.data

import android.content.Context
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.io.FileInputStream
import java.io.IOException

class TFLiteModel(context: Context) {

    private var interpreter: Interpreter

    init {
        interpreter = Interpreter(loadModelFile(context))
    }

    @Throws(IOException::class)
    private fun loadModelFile(context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd("diabetes_detect.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    // Fungsi untuk melakukan inferensi
    fun analyzeInput(features: FloatArray): Int {
        val output = Array(1) { FloatArray(1) } // Output untuk klasifikasi binary (0 atau 1)
        interpreter.run(features, output)
        return if (output[0][0] > 0.5) 1 else 0 // Threshold untuk binary classification
    }

}