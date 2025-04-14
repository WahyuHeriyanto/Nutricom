package org.wahyuheriyanto.nutricom.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.wahyuheriyanto.nutricom.R

@Composable

fun CustomProgressBar(
    labelName: String,
    current: Int,
    max: Double?
) {
    val safeMax = max ?: 1.0
    val progress = current.toFloat() / safeMax.toFloat()
    val progressPercentage = (progress * 100).toInt()

    val (color, label) = when {
        progressPercentage <= 70 -> Color(0xFF00AA16) to "Aman"
        progressPercentage < 100 -> Color.Yellow to "Mendekati Batas"
        else -> Color.Red to "Melewati Batas"
    }

    Column{
        Text(
            text = labelName,
            fontFamily = FontFamily(
                Font(
                    resId = R.font.inter_medium,
                    weight = FontWeight.Medium
                )
            ),
            fontSize = 15.sp,
            color = Color(android.graphics.Color.parseColor("#737373"))
        )
        Text(
            text = label,
            fontSize = 20.sp,
            color = color
        )
        Box(
            modifier = Modifier
                .width(200.dp)
                .height(6.dp) // Ukuran lebih besar agar lebih terlihat
                .clip(RoundedCornerShape(6.dp)) // Membuat ujung tumpul
                .background(Color.LightGray) // Warna dasar abu-abu
        ) {
            Box(
                modifier = Modifier
                    .fillMaxHeight()
                    .fillMaxWidth(progress) // Mengisi sesuai persen progress
                    .clip(RoundedCornerShape(6.dp))
                    .background(color)
            )
        }
    }
}