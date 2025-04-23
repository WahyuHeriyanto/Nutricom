package org.wahyuheriyanto.nutricom.view.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.wahyuheriyanto.nutricom.viewmodel.DetailScreeningViewModel
import org.wahyuheriyanto.nutricom.viewmodel.ScreeningDetailState
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DetailScreeningScreen(viewModel: DetailScreeningViewModel, type: String, documentId: String) {
    LaunchedEffect(Unit) {
        viewModel.loadDetail(type, documentId)
    }

    when (val state = viewModel.state) {
        is ScreeningDetailState.Loading -> Text("Loading...")
        is ScreeningDetailState.Error -> Text("Error: ${state.message}")
        is ScreeningDetailState.Umum -> DetailContent("Umum", state.data.timestamp, listOf(
            "Tinggi: ${state.data.height} cm",
            "Berat: ${state.data.weight} kg",
            "BMI: ${state.data.bmi}",
            "Tekanan Darah: ${state.data.apHi}/${state.data.apLo}",
            "Kolesterol: ${state.data.cholesterol}",
            "Glukosa: ${state.data.gluc}",
            "HbA1c: ${state.data.hba1c}",
            "Keluhan: ${state.data.healthComplaint ?: "-"}"
        ), "Hasil Pemeriksaan")

        is ScreeningDetailState.Diabetes -> DetailContent("Diabetes", state.data.timestamp, listOf(
            "Usia: ${state.data.age} tahun",
            "Jenis Kelamin: ${state.data.gender}",
            "Hipertensi: ${state.data.hypertension}",
            "Penyakit Jantung: ${state.data.heartDisease}",
            "BMI: ${state.data.bmi}",
            "HbA1c: ${state.data.hbA1c}",
            "Glukosa Darah: ${state.data.bloodGlucose}"
        ), "Prediksi: ${state.data.prediction}")

        is ScreeningDetailState.Cardio -> DetailContent("Kardiovaskular", state.data.timestamp, listOf(
            "Usia: ${state.data.age} tahun",
            "Tinggi: ${state.data.height} cm",
            "Berat: ${state.data.weight} kg",
            "Tekanan Darah: ${state.data.apHi}/${state.data.apLo}",
            "Kolesterol: ${state.data.cholesterol}",
            "Glukosa: ${state.data.gluc}"
        ), "Hasil Pemeriksaan")
    }
}

@Composable
fun DetailContent(type: String, timestamp: Long, items: List<String>, result: String) {
    val formattedDate = remember(timestamp) {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(timestamp))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Hasil Skrining $type", fontSize = 22.sp, color = Color.Black)
        Text(formattedDate, fontSize = 14.sp, color = Color.Gray)
        Spacer(modifier = Modifier.height(16.dp))

        items.forEach {
            Text(it, fontSize = 16.sp, color = Color.DarkGray, modifier = Modifier.padding(bottom = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(result, fontSize = 16.sp)
    }
}