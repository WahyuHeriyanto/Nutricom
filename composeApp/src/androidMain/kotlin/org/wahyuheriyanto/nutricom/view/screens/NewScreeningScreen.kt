package org.wahyuheriyanto.nutricom.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.viewmodel.submitScreening

@Composable
fun NewScreeningScreen(navController: NavController) {
    val context = LocalContext.current
    var currentStep by remember { mutableStateOf(0) }
    val inputValues = remember { mutableStateListOf("", "", "", "", "", "", "", "") }
    val placeholderList = listOf(
        "Masukkan usia",
        "Masukkan berat badan",
        "Masukkan tinggi badan",
        "Riwayat merokok (0-4)",
        "Konsumsi alkohol (0 atau 1)",
        "Riwayat penyakit jantung/kardiovaskular (0 atau 1)",
        "Riwayat penyakit diabetes (0 atau 1)",
        "Aktivitas harian (1.2 - 1.9)"
    )

    val imageList = listOf(
        R.drawable.diabetes_pictures, R.drawable.garam, R.drawable.gula,
        R.drawable.jenuh, R.drawable.kolesterol, R.drawable.lemak,
        R.drawable.scan, R.drawable.tidak_berpotensi_diabetes
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text("Skrining Awal", fontWeight = FontWeight.Bold, fontSize = 20.sp)

        Spacer(modifier = Modifier.height(16.dp))

        Image(
            painter = painterResource(id = imageList.getOrElse(currentStep) { imageList[0] }),
            contentDescription = "Gambar $currentStep",
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .align(Alignment.CenterHorizontally),
            contentScale = ContentScale.Fit
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Masukan data", fontSize = 16.sp)
            Text(
                text = "Skip",
                color = Color.Blue,
                modifier = Modifier.clickable {
                    inputValues[currentStep] = "0"
                    if (currentStep < 7) currentStep++ else {
                        submitScreening(inputValues.map { it.toFloatOrNull() ?: 0f })
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = inputValues[currentStep],
            onValueChange = { inputValues[currentStep] = it },
            placeholder = { Text(placeholderList[currentStep]) },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentStep > 0) {
                Button(onClick = { currentStep-- }) {
                    Text("Kembali")
                }
            } else {
                Spacer(modifier = Modifier.width(8.dp))
            }

            Button(onClick = {
                if (currentStep < 7) {
                    currentStep++
                } else {
                    submitScreening(inputValues.map { it.toFloatOrNull() ?: 0f })
                    navController.navigate("home")
                }
            }) {
                Text(if (currentStep == 6) "Selesai" else "Lanjut")
            }
        }
    }
}
