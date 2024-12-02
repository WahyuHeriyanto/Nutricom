package org.wahyuheriyanto.nutricom.data

import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel
import java.io.FileInputStream
import java.io.IOException

class HealthPredictionModel(context: Context) {
    private val interpreter: Interpreter

    init {
        interpreter = Interpreter(loadModelFile(context))
    }

    @Throws(IOException::class)
    private fun loadModelFile(context: Context): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd("kesehatan_predict_model.tflite")
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, fileDescriptor.startOffset, fileDescriptor.declaredLength)

    }

    fun predict(input: FloatArray): Array<FloatArray> {
        if (input.size != 6) {
            throw IllegalArgumentException("Input harus memiliki 6 nilai.")
        }
        val output = Array(1) { FloatArray(6) }
        interpreter.run(input, output)
        return output
    }
}
