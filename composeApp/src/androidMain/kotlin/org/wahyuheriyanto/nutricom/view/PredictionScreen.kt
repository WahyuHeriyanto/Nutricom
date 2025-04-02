package org.wahyuheriyanto.nutricom.view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.wahyuheriyanto.nutricom.viewmodel.HealthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.PredictionViewModel
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.roundToInt

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.wahyuheriyanto.nutricom.data.InputData

@Composable
fun CustomLineChart(data: List<Float>, modifier: Modifier = Modifier) {
    if (data.isEmpty()) return

    Canvas(modifier = modifier) {
        val width = size.width
        val height = size.height

        val maxDataValue = data.maxOrNull() ?: 1f
        val minDataValue = data.minOrNull() ?: 0f
        val range = maxDataValue - minDataValue

        // Scale data into chart coordinates
        val points = data.mapIndexed { index, value ->
            val x = index * (width / (data.size - 1))
            val y = height - ((value - minDataValue) / range * height)
            Offset(x, y)
        }

        // Draw lines
        val path = Path().apply {
            moveTo(points.first().x, points.first().y)
            for (point in points.drop(1)) {
                lineTo(point.x, point.y)
            }
        }
        drawPath(
            path = path,
            color = Color.Red, // Gunakan parameter `color` dengan nilai `Color`
            style = Stroke(width = 4.dp.toPx())
        )

        // Draw points
        points.forEach { point ->
            drawCircle(
                color = Color.Blue,
                radius = 6.dp.toPx(),
                center = point
            )
        }
    }
}

@Composable
fun PredictionScreen(viewModel: PredictionViewModel = viewModel()) {
    // Variabel inputData diinisialisasi tanpa nilai default
    var inputData by remember { mutableStateOf(InputData("", 0f, 0f, 0f, 0f, 0f, 0f)) }
    var data by remember { mutableStateOf(listOf<Float>()) }
    val prediction by viewModel.predictionResult.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState()) // Menambahkan scroll
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = inputData.date,
            onValueChange = { inputData = inputData.copy(date = it) },
            label = { Text("Date (dd/mm/yyyy)") }
        )
        OutlinedTextField(
            value = inputData.weight.toString(),
            onValueChange = { inputData = inputData.copy(weight = it.toFloat()) },
            label = { Text("Weight") }
        )
        OutlinedTextField(
            value = inputData.bloodSugar.toString(),
            onValueChange = { inputData = inputData.copy(bloodSugar = it.toFloat()) },
            label = { Text("Blood Sugar") }
        )
        OutlinedTextField(
            value = inputData.bloodPressureSystolic.toString(),
            onValueChange = { inputData = inputData.copy(bloodPressureSystolic = it.toFloat()) },
            label = { Text("Blood Pressure Systolic") }
        )
        OutlinedTextField(
            value = inputData.bloodPressureDiastolic.toString(),
            onValueChange = { inputData = inputData.copy(bloodPressureDiastolic = it.toFloat()) },
            label = { Text("Blood Pressure Diastolic") }
        )
        OutlinedTextField(
            value = inputData.cholesterol.toString(),
            onValueChange = { inputData = inputData.copy(cholesterol = it.toFloat()) },
            label = { Text("Cholesterol") }
        )
        OutlinedTextField(
            value = inputData.heartRate.toString(),
            onValueChange = { inputData = inputData.copy(heartRate = it.toFloat()) },
            label = { Text("Heart Rate") }
        )

        Button(onClick = {
            viewModel.getPrediction(inputData) // Kirim input ke API
        }) {
            Text("Predict")
        }

//        Spacer(modifier = Modifier.height(16.dp))
//
//        // Tampilkan hasil prediksi dalam bentuk grafik
//        prediction?.let {
//            CustomLineChart(data = it)
//        }
    }
}




