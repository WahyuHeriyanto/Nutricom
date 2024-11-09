package org.wahyuheriyanto.nutricom.view

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.util.Calendar
import java.util.Locale

@Composable
fun ScheduleScreen() {
    // State untuk menyimpan waktu yang diperbarui setiap detik
    var currentTime by remember { mutableStateOf(Calendar.getInstance().time) }

    // LaunchedEffect untuk memperbarui waktu setiap detik
    LaunchedEffect(Unit) {
        while (true) {
            currentTime = Calendar.getInstance().time
            delay(1000) // Update setiap 1 detik
        }
    }

    // Format hari, tanggal, dan waktu
    val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(currentTime)
    val date = SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(currentTime)
    val time = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(currentTime)

    ConstraintLayout(
        modifier = Modifier.fillMaxSize()
    ) {
        val (title, dateText, timeText) = createRefs()

        val startGuideline = createGuidelineFromStart(0.1f)
        val topGuideline = createGuidelineFromTop(0.1f)

        Text(
            text = "My Schedule",
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.constrainAs(title) {
                top.linkTo(topGuideline)
                start.linkTo(startGuideline)
            }
        )

        Text(
            text = "Date: $dayOfWeek, $date",
            fontSize = 20.sp,
            modifier = Modifier.constrainAs(dateText) {
                top.linkTo(title.bottom, margin = 16.dp)
                start.linkTo(startGuideline)
            }
        )

        Text(
            text = "Time: $time",
            fontSize = 20.sp,
            modifier = Modifier.constrainAs(timeText) {
                top.linkTo(dateText.bottom, margin = 8.dp)
                start.linkTo(startGuideline)
            }
        )
    }
}

@Preview
@Composable

fun SchedulePreview(){
    Surface (modifier = Modifier.fillMaxSize()){
        ScheduleScreen()
    }
}