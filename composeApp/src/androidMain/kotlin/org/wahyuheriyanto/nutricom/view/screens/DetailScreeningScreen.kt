package org.wahyuheriyanto.nutricom.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.wahyuheriyanto.nutricom.viewmodel.DetailScreeningViewModel
import org.wahyuheriyanto.nutricom.viewmodel.ScreeningDetailState
import org.wahyuheriyanto.nutricom.viewmodel.deleteScreeningItem
import org.wahyuheriyanto.nutricom.viewmodel.fetchDataHealth
import org.wahyuheriyanto.nutricom.viewmodel.fetchUserProfile
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun DetailScreeningScreen(navController: NavController, viewModel: DetailScreeningViewModel, type: String, documentId: String) {

    LaunchedEffect(Unit) {
        viewModel.loadDetail(type, documentId)
    }

    when (val state = viewModel.state) {
        is ScreeningDetailState.Loading -> Text("Loading...")
        is ScreeningDetailState.Error -> Text("Error: ${state.message}")
        is ScreeningDetailState.Umum -> DetailContent(navController = navController,"Umum", state.data.timestamp,state.data.id, listOf(
            "Tinggi: ${state.data.height} cm",
            "Berat: ${state.data.weight} kg",
            "BMI: ${state.data.bmi}",
            "Tekanan Darah: ${state.data.apHi}/${state.data.apLo}",
            "Kolesterol: ${state.data.cholesterol}",
            "Glukosa: ${state.data.gluc}",
            "HbA1c: ${state.data.hba1c}"
        ), "Keluhan: ${state.data.healthComplaint ?: "-"}")

        is ScreeningDetailState.Diabetes -> DetailContent(navController = navController,"Diabetes", state.data.timestamp, state.data.id, listOf(
            "Usia: ${state.data.age} tahun",
            "Jenis Kelamin: ${state.data.gender}",
            "Hipertensi: ${state.data.hypertension}",
            "Penyakit Jantung: ${state.data.heartDisease}",
            "BMI: ${state.data.bmi}",
            "HbA1c: ${state.data.hbA1c}",
            "Glukosa Darah: ${state.data.bloodGlucose}"
        ), "Prediksi: ${state.data.prediction}")

        is ScreeningDetailState.Cardio -> DetailContent(navController = navController,"Kardiovaskular", state.data.timestamp, state.data.id, listOf(
            "Usia: ${state.data.age} tahun",
            "Tinggi: ${state.data.height} cm",
            "Berat: ${state.data.weight} kg",
            "Tekanan Darah: ${state.data.apHi}/${state.data.apLo}",
            "Kolesterol: ${state.data.cholesterol}",
            "Glukosa: ${state.data.gluc}"
        ), "Prediksi: ${state.data.prediction}")
    }
}

@Composable
fun DetailContent(navController: NavController, type: String, timestamp: Long, idItem : String,  items: List<String>, result: String) {
    var showDialog by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Konfirmasi") },
            text = { Text("Apakah anda yakin ingin menghapus data ini?", textAlign = TextAlign.Justify
            ) },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    deleteScreeningItem(idItem)
                    navController.navigate("skrining")
                }) {
                    Text("Ya, hapus ", color = Color(android.graphics.Color.parseColor("#00AA16")))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text("Batalkan", color = Color(android.graphics.Color.parseColor("#00AA16")))
                }
            }
        )
    }

    val formattedDate = remember(timestamp) {
        SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(Date(timestamp))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Column (modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally){
            Text("Hasil Skrining $type", fontSize = 22.sp, color = Color.Black)
            Text(formattedDate, fontSize = 14.sp, color = Color.Gray)
        }
        Spacer(modifier = Modifier.height(16.dp))

        items.forEach {
            Text(it, fontSize = 16.sp, color = Color.DarkGray, modifier = Modifier.padding(bottom = 8.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(result, fontSize = 16.sp)
        Spacer(modifier = Modifier.height(25.dp))
        Box (modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center){
            Button(onClick = {
                showDialog = true
            }, modifier = Modifier.padding(top = 16.dp)
                .background(Color.Transparent),
                colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#00AA16")))
            ) {
                Text(text = "Hapus")
            }
        }

    }
}