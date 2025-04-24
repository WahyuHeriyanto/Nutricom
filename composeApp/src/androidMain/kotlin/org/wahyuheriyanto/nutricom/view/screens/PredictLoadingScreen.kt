package org.wahyuheriyanto.nutricom.view.screens

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import kotlinx.coroutines.delay
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.viewmodel.DiabetesViewModel

@Composable
fun PredictLoadingScreen(
    navController: NavController,
    viewModel: DiabetesViewModel,
    gender: Int,
    age: Int,
    hypertension: Int,
    heartDisease: Int,
    smokingHistory: Int,
    bmi: Float,
    hbA1c: Float,
    bloodGlucose: Int
) {
    val prediction by viewModel.prediction.collectAsState()
    val hasNavigated = remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.chip))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    var dotCount by remember { mutableStateOf(1) }

    // Jalankan prediksi saat komposisi dimulai
    LaunchedEffect(Unit) {
        Log.d("PredictLoadingScreen", "Memulai prediksi")
        Log.e("cekisipredict","$gender,$age, $hypertension, $heartDisease,$smokingHistory, $bmi, $hbA1c, $bloodGlucose")
        viewModel.predictDiabetes(
            gender, age, hypertension, heartDisease,
            smokingHistory, bmi, hbA1c, bloodGlucose
        )
    }

    // Looping titik-titik
    LaunchedEffect(Unit) {
        while (true) {
            delay(500L)
            dotCount = (dotCount % 3) + 1
        }
    }

    // Navigasi otomatis setelah hasil tersedia
    LaunchedEffect(prediction) {
        if (prediction != null && !hasNavigated.value) {
            hasNavigated.value = true
            delay(3000) // Biar animasi loading sempat tampil sebentar
            navController.navigate("resultPredictScreen")
        }
    }

    // UI Loading
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            LottieAnimation(
                composition = composition,
                progress = { progress },
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Memprediksi hasil" + ".".repeat(dotCount),
                fontWeight = FontWeight.Bold
            )
        }
    }
}
