package org.wahyuheriyanto.nutricom.view.widget

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp


    @Composable
    fun LineChart( width : Int, height : Int, data : List<Float>, date : List<String>){
        Box(modifier = Modifier.width(width.dp)
            .height(height.dp)
        ){
            // Data untuk grafik
            val datas = data
            val dates = date
            val maxY = 45f // Batas maksimum skala Y



            Canvas(modifier = Modifier.fillMaxSize()) {
                // Ukuran canvas
                val canvasWidth = size.width
                val canvasHeight = size.height

                // Margin untuk sumbu
                val marginX = 50f
                val marginY = 40f

                // Ukuran area grafik
                val graphWidth = canvasWidth - marginX
                val graphHeight = canvasHeight - marginY

                // Skala untuk data
                val stepX = graphWidth / (datas.size - 1)
                val scaleY = graphHeight / maxY

                // Sumbu X dan Y
                drawLine(
                    color = Color.Black,
                    start = Offset(marginX, marginY),
                    end = Offset(marginX, canvasHeight - marginY),
                    strokeWidth = 2f
                )
                drawLine(
                    color = Color.Black,
                    start = Offset(marginX, canvasHeight - marginY),
                    end = Offset(canvasWidth, canvasHeight - marginY),
                    strokeWidth = 2f
                )

                // Titik-titik data
                val points = datas.mapIndexed { index, value ->
                    Offset(
                        x = marginX + index * stepX,
                        y = canvasHeight - marginY - (value * scaleY)
                    )
                }

                // Garis antar titik
                for (i in 0 until points.size - 1) {
                    drawLine(
                        color = Color.Blue,
                        start = points[i],
                        end = points[i + 1],
                        strokeWidth = 4f
                    )
                }

                // Titik data
                points.forEach { point ->
                    drawCircle(color = Color.Red, center = point, radius = 6f)
                }

                // Label sumbu X
                dates.forEachIndexed { index, label ->
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            label,
                            marginX + index * stepX - 10f,
                            canvasHeight - marginY + 20f,
                            Paint().apply {
                                color = android.graphics.Color.BLACK
                                textSize = 32f
                            }
                        )
                    }
                }

                // Label sumbu Y
                //listOf(0, 500, 1000, 1500, 2000)
                listOf(0, 10, 20, 30, 40).forEach { label ->
                    val yOffset = canvasHeight - marginY - (label * scaleY)
                    drawContext.canvas.nativeCanvas.apply {
                        drawText(
                            label.toString(),
                            marginX - 40f,
                            yOffset,
                            Paint().apply {
                                color = android.graphics.Color.BLACK
                                textSize = 32f
                            }
                        )
                    }
                }
            }
        }
    }
