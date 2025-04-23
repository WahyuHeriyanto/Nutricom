package org.wahyuheriyanto.nutricom.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import org.wahyuheriyanto.nutricom.viewmodel.CardioViewModel

@Composable
fun PredictLoadingScreenCardio(
    //{age}/{gender}/{height}/{weight}/{ap_hi}/{ap_lo}/{cholesterol}/{gluc}/{smoke}/{alco}/{active}
    navController: NavController,
    viewModel: CardioViewModel,
    age: Int,
    gender: Int,
   height: Int,
    weight: Int,
    apHi: Int,
    apLo: Int,
    cholesterol: Int,
    gluc: Int,
    smoke: Int,
    alco: Int,
    active: Int
) {
    val prediction by viewModel.prediction.collectAsState()
    val hasNavigated = remember { mutableStateOf(false) }

    // Jalankan prediksi saat komposisi dimulai
    LaunchedEffect(Unit) {
        Log.d("PredictLoadingScreen", "Memulai prediksi")
        viewModel.predictCardio(
            age, gender, height, weight,
            apHi, apLo, cholesterol, gluc, smoke, alco, active
        )
    }

    // Navigasi otomatis setelah hasil tersedia
    LaunchedEffect(prediction) {
        if (prediction != null && !hasNavigated.value) {
            hasNavigated.value = true
            delay(3000) // Biar animasi loading sempat tampil sebentar
            navController.navigate("resultPredictScreenCardio")
        }
    }

    // UI Loading
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            Spacer(modifier = Modifier.height(16.dp))
            Text("Memprediksi hasil...", fontWeight = FontWeight.Bold)
        }
    }
}