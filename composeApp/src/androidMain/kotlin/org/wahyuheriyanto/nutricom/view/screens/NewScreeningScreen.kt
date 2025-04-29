package org.wahyuheriyanto.nutricom.view.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
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
    var expanded by remember { mutableStateOf(false) }

    val placeholderList = listOf(
        "Masukkan usia",
        "Masukkan berat badan",
        "Masukkan tinggi badan",
        "Riwayat merokok",
        "Apakah konsumsi alkohol?",
        "Apakah punya penyakit jantung/kardiovaskular?",
        "Apakah punya penyakit diabetes?",
        "Seberapa sering olahraga?"
    )

    val imageList = listOf(
        R.drawable.age, R.drawable.weight, R.drawable.height,
        R.drawable.smoke, R.drawable.alcohol, R.drawable.cardio_image,
        R.drawable.diabetes_pictures, R.drawable.running
    )

    // Opsi Dropdown berbentuk Label -> Nilai
    val dropdownOptions: List<Pair<String, String>> = when (currentStep) {
        3 -> listOf( // Riwayat merokok (tetap angka)
            "Tidak pernah" to "0",
            "Mantan perokok aktif" to "1",
            "Perokok, aktif saat ini" to "2",
            "Pernah merokok" to "3",
            "Perokok, sedang tidak merokok" to "5",
            "Lainnya" to "4",
        )
        4, 5, 6 -> listOf( // Alkohol, Jantung, Diabetes (Ya/Tidak)
            "Ya" to "1",
            "Tidak" to "0"
        )
        7 -> listOf( // Aktivitas harian
            "Setiap hari" to "2",
            "Seminggu dua - empat kali" to "1.8",
            "Seminggu sekali" to "1.5",
            "Jarang olahraga" to "1"
        )
        else -> emptyList()
    }

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
            Text(placeholderList[currentStep], fontSize = 16.sp)
            Text(
                text = "Skip",
                color = Color.Blue,
                modifier = Modifier.clickable {
                    inputValues[currentStep] = "0"
                    if (currentStep < 7) {
                        currentStep++
                    } else {
                        submitScreening(inputValues.map { it.toFloatOrNull() ?: 0f })
                        navController.navigate("home")
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        if (dropdownOptions.isNotEmpty()) {
            Box {
                OutlinedTextField(
                    value = dropdownOptions.firstOrNull { it.second == inputValues[currentStep] }?.first
                        ?: "",
                    onValueChange = { },
                    readOnly = true,
                    placeholder = { Text(placeholderList[currentStep]) },
                    trailingIcon = {
                        Icon(
                            imageVector = if (expanded) Icons.Filled.ArrowDropDown else Icons.Filled.ArrowDropDown,
                            contentDescription = null,
                            modifier = Modifier.clickable { expanded = !expanded }
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { expanded = !expanded }
                )

                DropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    dropdownOptions.forEach { (label, value) ->
                        DropdownMenuItem(
                            onClick = {
                                inputValues[currentStep] = value
                                expanded = false
                            }
                        ) {
                            Text(label)
                        }
                    }
                }
            }
        } else {
            OutlinedTextField(
                value = inputValues[currentStep],
                onValueChange = { inputValues[currentStep] = it },
                placeholder = { Text(placeholderList[currentStep]) },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (currentStep > 0) {
                Button(onClick = { currentStep-- },
                    modifier = Modifier.background(Color.Transparent),
                    colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#00AA16")))) {
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
            },
            modifier = Modifier.background(Color.Transparent),
                colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#00AA16")))
            ) {
                Text(if (currentStep == 7) "Selesai" else "Lanjut")
            }
        }
    }
}



