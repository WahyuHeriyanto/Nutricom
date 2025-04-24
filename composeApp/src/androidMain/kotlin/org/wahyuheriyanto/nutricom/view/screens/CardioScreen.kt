package org.wahyuheriyanto.nutricom.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.view.factory.DiabetesViewModelFactory
import org.wahyuheriyanto.nutricom.viewmodel.DiabetesViewModel
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.ui.text.style.TextAlign
import org.wahyuheriyanto.nutricom.view.factory.CardioViewModelFactory
import org.wahyuheriyanto.nutricom.viewmodel.CardioInputData
import org.wahyuheriyanto.nutricom.viewmodel.CardioViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DataPredictViewModel
import org.wahyuheriyanto.nutricom.viewmodel.fetchDataHealth
import org.wahyuheriyanto.nutricom.viewmodel.fetchUserProfile

@Composable
fun CardioScreen(navController: NavController, dataPredictViewModel: DataPredictViewModel) {
    val context = LocalContext.current
    val viewModel: CardioViewModel = viewModel(factory =  CardioViewModelFactory(context))
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var apHi by remember { mutableStateOf("") }
    var apLo by remember { mutableStateOf("") }
    var cholesterol by remember { mutableStateOf("") }
    var gluc by remember { mutableStateOf("") }
    var smoke by remember { mutableStateOf("") }
    var alco by remember { mutableStateOf("") }
    var active by remember { mutableStateOf("") }
    val prediction by viewModel.prediction.collectAsState()
    var showDialog by remember { mutableStateOf(true) }
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        if (showDialog) {
            // Do nothing, wait for dialog
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Peringatan") },
            text = { Text("Prediksi ini tidak 100% akurat dan hanya berfungsi sebagai deteksi awal. Gunakan hasil ini sebagai referensi awal, bukan diagnosis medis. \n\n"
                    + "Jika Anda merasa memiliki keluhan atau gejala tertentu, segera konsultasikan ke fasilitas kesehatan terdekat untuk pemeriksaan lebih lanjut\n\n"
                    + "Data kesehatan terbaru telah ditemukan. Apakah anda ingin menggunakannya?", textAlign = TextAlign.Justify
            ) },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    fetchDataHealth { health ->
                        height = health.height.toString()
                        weight = health.weight.toString()
                        age = health.age.toString()
                        apHi = health.apHi.toString()
                        apLo = health.apLo.toString()
                        cholesterol = statusKolesterol(health.cholesterol.toFloat())
                        gluc = kategoriGlukosa(health.gluc.toFloat())
                        smoke = if (health.smokingHistory.toString() in listOf("0", "1", "4")) "0" else "1"
                        alco = health.alcoholConsume.toString()
                        active = if (health.activity.toString() in listOf("2", "1.8")) "1" else "0"

                    }
                    fetchUserProfile { profile ->
                        gender = if (profile.gender == "Laki-laki") "1" else "0"
                    }
                }) {
                    Text("Ya, gunakan", color = Color(android.graphics.Color.parseColor("#00AA16")))
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                }) {
                    Text("Buat data baru", color = Color(android.graphics.Color.parseColor("#00AA16")))
                }
            }
        )
    }


    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()
        .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(
            text = "Screening Resiko",
            fontFamily = FontFamily(
                Font(
                    resId = R.font.inter_semibold,
                    weight = FontWeight.Bold
                )
            ),
            fontSize = 22.sp,
            color = Color.Black
        )
        Text(
            text = "Kardiovaskular",
            fontFamily = FontFamily(
                Font(
                    resId = R.font.inter_semibold,
                    weight = FontWeight.Bold
                )
            ),
            fontSize = 22.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(15.dp))
        Image(painter = painterResource(id = R.drawable.cardio_image), contentDescription ="",
            modifier = Modifier
                .width(320.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(20.dp)),
            alignment = Alignment.Center
        )
        Spacer(modifier = Modifier.height(15.dp))

        Box(modifier = Modifier.fillMaxWidth()){
            Text(
                text = "Masukan data kesehatan anda",
                fontFamily = FontFamily(
                    Font(
                        resId = R.font.inter_medium,
                        weight = FontWeight.Medium
                    )
                ),
                fontSize = 18.sp,
                color = androidx.compose.ui.graphics.Color.Black
            )
        }
        CustomTextField("Umur", age, KeyboardType.Decimal) { age = it }
        CustomDropdownField(
            label = "Jenis Kelamin",
            options = listOf("Perempuan" to "0", "Laki-Laki" to "1"),
            selectedValue = gender,
            onValueChange = { gender = it }
        )
        CustomTextField("Tinggi Badan", height, KeyboardType.Decimal) { height = it }
        CustomTextField("Berat Badan", weight, KeyboardType.Decimal) { weight = it }
        CustomTextField("Tekanan Darah Sistolik (Atas) ", apHi, KeyboardType.Decimal) { apHi = it }
        CustomTextField("Tekanan Darah Diastolik (Bawah) ", apLo, KeyboardType.Decimal) { apLo = it }
        CustomDropdownField(
            label = "Kadar Kolesterol",
            options = listOf("Normal" to "0", "Tinggi" to "1", "Sangat Tinggi" to "2"),
            selectedValue = cholesterol,
            onValueChange = { cholesterol= it }
        )
        CustomDropdownField(
            label = "Kadar Glukosa",
            options = listOf("Normal" to "0", "Tinggi" to "1", "Sangat Tinggi" to "2"),
            selectedValue = gluc,
            onValueChange = { gluc = it }
        )
        CustomDropdownField(
            label = "Riwayat Merokok",
            options = listOf("Ya" to "1", "Tidak" to "0"),
            selectedValue = smoke,
            onValueChange = { smoke = it }
        )
        CustomDropdownField(
            label = "Riwayat Konsumsi Alkohol",
            options = listOf("Ya" to "1", "Tidak" to "0"),
            selectedValue = alco,
            onValueChange = { alco = it }
        )
        CustomDropdownField(
            label = "Riwayat Aktivitas Fisik",
            options = listOf("Ya" to "1", "Tidak" to "0"),
            selectedValue = active,
            onValueChange = { active = it }
        )

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){

            Button(
                onClick = {
                    navController.navigate("predictLoadingScreenCardio/${age}/${gender}/${height}/${weight}/${apHi}/${apLo}/${cholesterol}/${gluc}/${smoke}/${alco}/${active}")
                    dataPredictViewModel.saveInputDataCardio(CardioInputData(age.toDouble(),gender.toDouble(),height.toDouble(),weight.toDouble(),apHi.toDouble(),apLo.toDouble(), cholesterol.toDouble(),gluc.toDouble(),smoke.toDouble(),alco.toDouble(),active.toDouble()))
                          },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(Color.Transparent),
                colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#00AA16")))
            ) {
                Text("Prediksi", color = Color.White)
            }


            Spacer(modifier = Modifier.width(20.dp))

            Button(onClick = {
                navController.navigate("recommendationList")
            }, modifier = Modifier
                .padding(top = 16.dp)
                .background(Color.Transparent),
                colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#00AA16")))) {
                Text(text = "Rekomendasi",color = Color.White)
            }
        }

        prediction?.let {
            Text(
                text = if (it == 1) "Hasil: Diabetes" else "Hasil: Tidak Diabetes",
                color = if (it == 1) MaterialTheme.colors.error else MaterialTheme.colors.primary,
                modifier = Modifier.padding(top = 16.dp)
            )
        }
    }
}

fun statusKolesterol(kolesterol : Float): String = when {
    kolesterol < 200 -> "0"
    kolesterol >= 200 && kolesterol < 240 -> "1"
    else -> "2"
}

fun kategoriGlukosa(glukosa: Float): String = when {
    glukosa <= 140 -> "0"
    glukosa <= 199 -> "1"
    else -> "2"
}

