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
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.wahyuheriyanto.nutricom.viewmodel.HealthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.PredictionViewModel
import androidx.compose.ui.graphics.drawscope.Stroke
import kotlin.math.roundToInt

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
    var days by remember { mutableStateOf("5") }
    var data by remember { mutableStateOf(listOf<Float>()) }
    val prediction by viewModel.predictionResult.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(
            value = days,
            onValueChange = { days = it },
            label = { Text("Days to Predict") }
        )
        Button(onClick = {
            data = listOf(70f, 72f, 74f, 73f, 75f) // Contoh data input simulasi
            viewModel.predictTrend(listOf(floatArrayOf(70f, 110f, 120f, 80f, 180f, 72f)), days.toInt())
        }) {
            Text("Predict")
        }
        Spacer(modifier = Modifier.height(16.dp))
        CustomLineChart(data = prediction.flatMap { it.asList() })

    }
}

