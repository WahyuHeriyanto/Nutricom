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
import org.wahyuheriyanto.nutricom.viewmodel.DataPredictViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DiabetesInputData

@Composable
fun DiabetesScreen(navController: NavController, dataPredictViewModel: DataPredictViewModel) {
    val context = LocalContext.current
    val viewModel: DiabetesViewModel = viewModel(factory = DiabetesViewModelFactory(context))
    var gender by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var hypertension by remember { mutableStateOf("") }
    var heartDisease by remember { mutableStateOf("") }
    var smokingHistory by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf("") }
    var hbA1c by remember { mutableStateOf("") }
    var bloodGlucose by remember { mutableStateOf("") }
    val prediction by viewModel.prediction.collectAsState()
    val scrollState = rememberScrollState()


    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()
        .verticalScroll(scrollState),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally){
        Text(
            text = "Screening Resiko Diabetes",
            fontFamily = FontFamily(
                Font(
                    resId = R.font.inter_semibold,
                    weight = FontWeight.Bold
                )
            ),
            fontSize = 22.sp,
            color = androidx.compose.ui.graphics.Color.Black
        )
        Spacer(modifier = Modifier.height(15.dp))
        Image(painter = painterResource(id = R.drawable.diabetes_pictures), contentDescription ="",
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
        CustomDropdownField(
            label = "Gender",
            options = listOf("Perempuan" to "0", "Laki-Laki" to "1"),
            selectedValue = gender,
            onValueChange = { gender = it }
        )

        CustomTextField("Age", age, KeyboardType.Number) { age = it }
        CustomDropdownField(
            label = "Hypertension",
            options = listOf("Tidak" to "0", "Ya" to "1"),
            selectedValue = hypertension,
            onValueChange = { hypertension = it }
        )
        CustomDropdownField(
            label = "Heart Disease",
            options = listOf("Tidak" to "0", "Ya" to "1"),
            selectedValue = heartDisease,
            onValueChange = { heartDisease = it }
        )
        CustomDropdownField(
            label = "Smoking History",
            options = listOf(
                "Tidak pernah" to "0",
                "Mantan perokok aktif" to "1",
                "Perokok, aktif saat ini" to "2",
                "Pernah merokok" to "3",
                "Perokok, sedang tidak merokok" to "5",
                "Lainnya" to "4",
            ),
            selectedValue = smokingHistory,
            onValueChange = { smokingHistory = it }
        )
        CustomTextField("BMI", bmi, KeyboardType.Decimal) { bmi = it }
        CustomTextField("HbA1c Level", hbA1c, KeyboardType.Decimal) { hbA1c = it }
        CustomTextField("Blood Glucose Level", bloodGlucose, KeyboardType.Number) { bloodGlucose = it }

        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center){

            Button(
                onClick = {
                    navController.navigate("predictLoadingScreen/${gender}/${age}/${hypertension}/${heartDisease}/${smokingHistory}/${bmi}/${hbA1c}/${bloodGlucose}")
                    dataPredictViewModel.saveInputDataDiabetes(DiabetesInputData(gender.toDouble(),
                        age.toDouble(),
                        hypertension.toDouble(),
                        heartDisease.toDouble(),
                        smokingHistory.toDouble(),
                        bmi.toDouble(),
                        hbA1c.toDouble(),
                        bloodGlucose.toDouble()
                    ))
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

@Composable
fun CustomTextField(
    label: String,
    value: String,
    keyboardType: KeyboardType = KeyboardType.Text,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = Modifier
            .fillMaxWidth()  // Perbaikan letak modifier
            .padding(top = 8.dp)
    )
}

@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CustomDropdownField(
    label: String,
    options: List<Pair<String, String>>, // Text shown, actual value
    selectedValue: String,
    onValueChange: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val selectedLabel = options.find { it.second == selectedValue }?.first ?: ""

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 8.dp)
    ) {
        OutlinedTextField(
            value = selectedLabel,
            onValueChange = {},
            readOnly = true,
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor()
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            options.forEach { (display, value) ->
                DropdownMenuItem(
                    onClick = {
                        onValueChange(value)
                        expanded = false
                    }
                ) {
                    Text(display)
                }
            }
        }
    }
}


