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
        val fileDescriptor = context.assets.openFd("nutricom_model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    // Fungsi untuk melakukan inferensi
    fun analyzeInput(features: FloatArray): Int {
        val output = Array(1) { FloatArray(4) } // Output dengan 4 kelas
        interpreter.run(features, output)

        // Cari indeks dengan nilai maksimum di output[0]
        val maxIndex = output[0].withIndex().maxByOrNull { it.value }?.index ?: -1
        return maxIndex
    }

}