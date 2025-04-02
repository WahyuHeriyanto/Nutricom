package org.wahyuheriyanto.nutricom.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.view.factory.DiabetesViewModelFactory
import org.wahyuheriyanto.nutricom.viewmodel.DiabetesViewModel

@Composable
fun DiabetesScreen() {
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
        .verticalScroll(scrollState)) {
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
        Image(painter = painterResource(id = R.drawable.scan), contentDescription ="",
            modifier = Modifier
                .width(320.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(20.dp)),
            alignment = Alignment.Center
        )
        Spacer(modifier = Modifier.height(15.dp))

        Text(
            text = "Prediksi Diabetes",
            fontFamily = FontFamily(
                Font(
                    resId = R.font.inter_semibold,
                    weight = FontWeight.Bold
                )
            ),
            fontSize = 20.sp,
            color = androidx.compose.ui.graphics.Color.Black
        )

        CustomTextField("Gender (0: Female, 1: Male)", gender) { gender = it }
        CustomTextField("Age", age, KeyboardType.Number) { age = it }
        CustomTextField("Hypertension (0: No, 1: Yes)", hypertension, KeyboardType.Number) { hypertension = it }
        CustomTextField("Heart Disease (0: No, 1: Yes)", heartDisease, KeyboardType.Number) { heartDisease = it }
        CustomTextField("Smoking History (1-4)", smokingHistory, KeyboardType.Number) { smokingHistory = it }
        CustomTextField("BMI", bmi, KeyboardType.Decimal) { bmi = it }
        CustomTextField("HbA1c Level", hbA1c, KeyboardType.Decimal) { hbA1c = it }
        CustomTextField("Blood Glucose Level", bloodGlucose, KeyboardType.Number) { bloodGlucose = it }

        Row(horizontalArrangement = Arrangement.Center){
            Button(
                onClick = {
                    viewModel.predictDiabetes(
                        gender.toIntOrNull() ?: 0,
                        age.toIntOrNull() ?: 0,
                        hypertension.toIntOrNull() ?: 0,
                        heartDisease.toIntOrNull() ?: 0,
                        smokingHistory.toIntOrNull() ?: 1,
                        bmi.toFloatOrNull() ?: 0f,
                        hbA1c.toFloatOrNull() ?: 0f,
                        bloodGlucose.toIntOrNull() ?: 0
                    )
                },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                Text("Prediksi")
            }
            Spacer(modifier = Modifier.width(20.dp))

            Button(onClick = { /*TODO*/ }, modifier = Modifier.padding(top = 16.dp)) {
                Text(text = "Rekomendasi")
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

