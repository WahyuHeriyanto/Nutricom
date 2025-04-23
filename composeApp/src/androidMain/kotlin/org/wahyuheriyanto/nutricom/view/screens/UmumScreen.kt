package org.wahyuheriyanto.nutricom.view.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.view.components.CustomCircularProgressBar
import org.wahyuheriyanto.nutricom.view.components.HealthCircularProgressBar
import org.wahyuheriyanto.nutricom.view.factory.CardioViewModelFactory
import org.wahyuheriyanto.nutricom.viewmodel.CardioInputData
import org.wahyuheriyanto.nutricom.viewmodel.CardioViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DataPredictViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DataViewModel
import org.wahyuheriyanto.nutricom.viewmodel.UmumInputData
import org.wahyuheriyanto.nutricom.viewmodel.fetchDataHealth

@Composable
fun UmumScreen(navController: NavController, dataPredictViewModel: DataPredictViewModel) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
//    val heightMeter = height.toFloat() / 100f
    var bmi by remember { mutableStateOf("") }
    var apHi by remember { mutableStateOf("") }
    var apLo by remember { mutableStateOf("") }
//    val bloodPresCal = apHi.toFloat() - apLo.toFloat()
    var bloodPressure by remember { mutableStateOf("") }
    var cardio by remember { mutableStateOf("") }
    var diabetes by remember { mutableStateOf("") }
    var cholesterol by remember { mutableStateOf("") }
    var gluc by remember { mutableStateOf("") }
    var smoke by remember { mutableStateOf("") }
    var alco by remember { mutableStateOf("") }
    var active by remember { mutableStateOf("") }
    var sleep by remember { mutableStateOf("") }
    var ldl by remember { mutableStateOf("") }
    var hdl by remember { mutableStateOf("") }
    var tri by remember { mutableStateOf("") }
    var hba1c by remember { mutableStateOf("") }
    var healthComplaint by remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    var showDialog by remember { mutableStateOf(true) }
    var showProcessing by remember { mutableStateOf(false) }
    var showResult by remember { mutableStateOf(false) }
    var resultText by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        if (showDialog) {
            // Do nothing, wait for dialog
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Skrining Kesehatan Umum") },
            text = { Text("Data kesehatan terbaru telah ditemukan. Apakah anda ingin mengunduh data yang telah terekam?") },
            confirmButton = {
                TextButton(onClick = {
                    showDialog = false
                    fetchDataHealth { health ->
                        height = health.height.toString()
                        weight = health.weight.toString()
                        //if (heightMeter != 0f) weight / (heightMeter * heightMeter) else 0f
                        bmi = health.bmi.toString()
                        apHi = health.apHi.toString()
                        apLo = health.apLo.toString()
                        //belum bikin tekanan nadi
                        cardio = health.cardio.toString()
                        diabetes = health.diabetes.toString()
                        cholesterol = health.cholesterol.toString()
                        gluc = health.gluc.toString()
                        smoke = health.smokingHistory.toString()
                        alco = health.alcoholConsume.toString()
                        active = health.activity.toString()
                        sleep = health.sleep.toString()
                        ldl = health.ldl.toString()
                        hdl = health.hdl.toString()
                        tri = health.tri.toString()
                        hba1c = health.hba1c.toString()
                        healthComplaint = health.healthComplaint
                    }
                }) {
                    Text("Ya, gunakan")
                }
            },
            dismissButton = {
                TextButton(onClick = {
                    showDialog = false
                    fetchDataHealth { health ->
                        bmi = health.bmi.toString()
                        healthComplaint = health.healthComplaint
                    }
                }) {
                    Text("Buat data baru")
                }
            }
        )
    }

    if (showProcessing) {
        AlertDialog(
            onDismissRequest = {},
            title = { Text("Mohon tunggu") },
            text = {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("Data sedang diolah mohon tunggu sebentar")
                    Spacer(modifier = Modifier.height(16.dp))
                    CircularProgressIndicator()
                }
            },
            buttons = {}
        )
    }

    if (showResult) {
        AlertDialog(
            onDismissRequest = { showResult = false },
            title = { Text("Hasil Pemeriksaan") },
            text = { Text(resultText) },
            confirmButton = {
                TextButton(onClick = { showResult = false }) {
                    Text("Tutup")
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
            text = "Screening Umum",
            fontFamily = FontFamily(
                Font(
                    resId = R.font.inter_semibold,
                    weight = FontWeight.Bold
                )
            ),
            fontSize = 22.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(20.dp))
        Row (modifier = Modifier.fillMaxWidth()){
            Spacer(modifier = Modifier.width(20.dp))
            HealthCircularProgressBar(current = bmi.toFloatOrNull() ?: 0f, max = 40f)
            Spacer(modifier = Modifier.width(15.dp))
            Column {
                Text(text = "Hasil pemeriksaan kesehatan", fontSize = 14.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = healthComplaint, fontSize = 10.sp, textAlign = TextAlign.Justify)
            }
        }
        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier.fillMaxWidth()){
            Text(
                text = "Data Kesehatan Fisik",
                fontFamily = FontFamily(
                    Font(
                        resId = R.font.inter_medium,
                        weight = FontWeight.Medium
                    )
                ),
                fontSize = 16.sp,
                color = Color.Black
            )
        }
        CustomTextField("Tinggi badan", height, KeyboardType.Decimal) { height = it }
        CustomTextField("Berat badan", weight, KeyboardType.Decimal) { weight = it }
//        CustomTextField("BMI", (weight.toFloat() / (heightMeter * heightMeter)).toString(), KeyboardType.Decimal) { bmi = it }
        CustomTextField("Tekanan darah Sistolik (Atas)", apHi, KeyboardType.Decimal) { apHi = it }
        CustomTextField("Tekanan darah Diastolik (Bawah)", apLo, KeyboardType.Decimal) { apLo = it }
//        CustomTextField("Tekanan Nadi ", bloodPresCal.toString(), KeyboardType.Decimal) { bloodPressure = it }
        CustomDropdownField(
            label = "Riwayat penyakit jantung/kardiovaskular",
            options = listOf("ya" to "0", "tidak" to "1"),
            selectedValue = cardio,
            onValueChange = { cardio = it }
        )
        CustomDropdownField(
            label = "Riwayat penyakit diabetes melitus",
            options = listOf("ya" to "0", "tidak" to "1"),
            selectedValue = diabetes,
            onValueChange = { diabetes = it }
        )


        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.fillMaxWidth()){
            Text(
                text = "Data Aktifitas dan Gaya Hidup",
                fontFamily = FontFamily(
                    Font(
                        resId = R.font.inter_medium,
                        weight = FontWeight.Medium
                    )
                ),
                fontSize = 16.sp,
                color = Color.Black
            )
        }
        CustomDropdownField(
            label = "Riwayat Merokok",
            options = listOf("tidak" to "0", "Ya" to "1"),
            selectedValue = smoke,
            onValueChange = { smoke = it }
        )
        CustomDropdownField(
            label = "Riwayat Konsumsi Alkohol",
            options = listOf("tidak" to "0", "Ya" to "1"),
            selectedValue = alco,
            onValueChange = { alco = it }
        )
        CustomDropdownField(
            label = "Frekuensi Olahraga",
            options = listOf("Setiap hari" to "2", "Seminggu dua - empat kali" to "1.8",
                "Seminggu sekali" to "1.5", "Jarang olahraga" to "1" ),
            selectedValue = active,
            onValueChange = { active = it }
        )
        CustomTextField("Frekuensi Jam Tidur (dalam jam)", sleep, KeyboardType.Decimal) { sleep = it }

        Spacer(modifier = Modifier.height(12.dp))

        Box(modifier = Modifier.fillMaxWidth()){
            Text(
                text = "Data Tes Lab",
                fontFamily = FontFamily(
                    Font(
                        resId = R.font.inter_medium,
                        weight = FontWeight.Medium
                    )
                ),
                fontSize = 16.sp,
                color = Color.Black
            )
        }
        CustomTextField("Kadar LDL dalam darah", ldl, KeyboardType.Decimal) { ldl = it }
        CustomTextField("Kadar HDL dalam darah", hdl, KeyboardType.Decimal) { hdl = it }
        CustomTextField("Kadar Triglyceride dalam darah", tri, KeyboardType.Decimal) { tri = it }
        CustomTextField("Kadar Kolesterol total", cholesterol, KeyboardType.Decimal) { cholesterol = it }
        CustomTextField("Kadar gula dalam darah", gluc, KeyboardType.Decimal) { gluc = it }
        CustomTextField("HbA1c", hba1c, KeyboardType.Decimal) { hba1c = it }


        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){

            Button(
                onClick = {
                    dataPredictViewModel.sendDataUmumToFirestore(UmumInputData(height.toDouble(),weight.toDouble(),bmi.toDouble(),
                        apHi.toDouble(),apLo.toDouble(),cardio.toDouble(),diabetes.toDouble(),cholesterol.toDouble(),gluc.toDouble(),
                        smoke.toDouble(),alco.toDouble(), active.toDouble(),sleep.toDouble(),ldl.toDouble(),hdl.toDouble(),tri.toDouble(),hba1c.toDouble(), generateHealthComplaint(
                            kategoriBmi(bmi.toFloatOrNull() ?: 0f), kategoriTekananDarah(apHi.toFloatOrNull() ?: 0f, apLo.toFloatOrNull() ?: 0f),
                            kategoriGula(gluc.toFloatOrNull() ?: 0f), analisisGayaHidup(active.toFloatOrNull() ?: 0f, smoke, alco, sleep.toFloatOrNull() ?: 0f)
                        )
                    ), onSuccess = {
                        Toast.makeText(context, "Berhasil disimpan!", Toast.LENGTH_SHORT).show()
                    },
                        onError = {
                            Toast.makeText(context, "Gagal: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                        })
                    showProcessing = true
                    coroutineScope.launch {
                        delay(3000)
                        showProcessing = false
                        showResult = true
                        resultText = """
                        Berdasarkan hasil pemeriksaan:
                        - Massa tubuh anda masuk dalam kategori ${kategoriBmi(bmi.toFloatOrNull() ?: 0f)}.
                        - Tekanan darah anda masuk kategori ${kategoriTekananDarah(apHi.toFloatOrNull() ?: 0f, apLo.toFloatOrNull() ?: 0f)}.
                        - Gula darah anda masuk kategori ${kategoriGula(gluc.toFloatOrNull() ?: 0f)}.
                        - Gaya hidup anda ${analisisGayaHidup(active.toFloatOrNull() ?: 0f, smoke, alco, sleep.toFloatOrNull() ?: 0f)}.

                        Rekomendasi kesehatan telah ditambahkan, silakan cek dan ikuti.
                    """.trimIndent()
                    }
                },
                modifier = Modifier
                    .padding(top = 16.dp)
                    .background(Color.Transparent),
                colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#00AA16")))
            ) {
                Text("Cek Kesehatan", color = Color.White)
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
    }

}

fun kategoriBmi(bmi: Float): String = when {
    bmi < 18.5 -> "Underweight"
    bmi < 24.9 -> "Normal"
    bmi < 29.9 -> "Overweight"
    else -> "Obesitas"
}

fun kategoriTekananDarah(sistolik: Float, diastolik: Float): String = when {
    sistolik < 90 || diastolik < 60 -> "Rendah"
    sistolik <= 120 && diastolik <= 80 -> "Normal"
    sistolik <= 139 || diastolik <= 89 -> "Pra-hipertensi"
    else -> "Hipertensi"
}

fun kategoriGula(glukosa: Float): String = when {
    glukosa < 70 -> "Rendah"
    glukosa <= 140 -> "Normal"
    glukosa <= 199 -> "Pra-diabetes"
    else -> "Diabetes"
}

fun analisisGayaHidup(activity: Float, smoke: String, alco: String, sleep: Float): String {
    val isAktif = activity >= 1.75
    val isMerokok = smoke != "0"
    val isMinum = alco != "0"
    val tidurCukup = sleep in 7.0..9.0

    return when {
        isAktif && !isMerokok && !isMinum && tidurCukup -> "sehat"
        isAktif && (isMerokok || isMinum) && tidurCukup -> "cenderung aktif meski punya kebiasaan kurang sehat"
        !isAktif && (isMerokok || isMinum) && !tidurCukup -> "pola hidup kurang sehat"
        !tidurCukup -> "kurang tidur"
        else -> "perlu perbaikan gaya hidup"
    }
}

fun generateHealthComplaint(
    bmiCategory: String,
    tekananDarahCategory: String,
    gulaCategory: String,
    gayaHidup: String
): String {
    val complaints = mutableListOf<String>()

    when (bmiCategory) {
        "Underweight" -> complaints.add("mengalami kekurangan berat badan")
        "Overweight", "Obesitas" -> complaints.add("mengalami kelebihan berat badan")
    }

    when (tekananDarahCategory) {
        "Rendah" -> complaints.add("mengalami tekanan darah rendah")
        "Pra-hipertensi" -> complaints.add("berisiko tekanan darah tinggi")
        "Hipertensi" -> complaints.add("mengalami tekanan darah tinggi")
    }

    when (gulaCategory) {
        "Pra-diabetes" -> complaints.add("berisiko diabetes")
        "Diabetes" -> complaints.add("mengalami diabetes")
    }

    if (gayaHidup in listOf("pola hidup kurang sehat", "kurang tidur", "perlu perbaikan gaya hidup")) {
        complaints.add("memiliki pola hidup yang perlu diperbaiki")
    }

    return when (complaints.size) {
        0 -> "Anda dalam kondisi kesehatan yang baik secara umum."
        1 -> "Anda ${complaints[0]}"
        2 -> "Anda ${complaints[0]} dan ${complaints[1]}"
        else -> {
            val last = complaints.removeLast()
            "Anda ${complaints.joinToString(", ")} dan $last"
        }
    }
}


