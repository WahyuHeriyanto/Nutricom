package org.wahyuheriyanto.nutricom.view.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomCircularProgressBar(
    current: Float?,
    max: Float?
) {
    val progress = (current ?: 0f) / (max ?: 2200f)
    val progressPercentage = (progress * 100).toInt()

    val (color, label) = when {
        progressPercentage <= 30 -> Color.Red to "Rendah"
        progressPercentage <= 65 -> Color.Yellow to "Kurang"
        progressPercentage >= 95 -> Color(0xFF00AA16) to "Terpenuhi"
        else -> Color.Blue to "Cukup"
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(85.dp)
    ) {
        CircularProgressIndicator(
            progress = progress,
            color = color,
            strokeWidth = 6.dp,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = label,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun HealthCircularProgressBar(
    current: Float,
    max: Float
) {
    val progress = current / max

    val (color, label) = when {
        current <= 18.5 -> Color(android.graphics.Color.parseColor("#FFB700")) to "Underweight"
        current > 18.5 && current < 24.9 -> Color(0xFF00AA16 )to "Normal"
        current >= 25 && current < 29.9 -> Color.Yellow to "Overweight"
        else -> Color.Red to "Obesitas"
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(85.dp)
    ) {
        CircularProgressIndicator(
            progress = progress,
            color = color,
            strokeWidth = 6.dp,
            modifier = Modifier.fillMaxSize()
        )
        Text(
            text = label,
            color = color,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}