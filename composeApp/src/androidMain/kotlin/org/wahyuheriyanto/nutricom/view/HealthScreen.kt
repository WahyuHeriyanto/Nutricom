package org.wahyuheriyanto.nutricom.view


import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.wahyuheriyanto.nutricom.viewmodel.HealthViewModel


@Composable
fun HealthScreen(viewModel: HealthViewModel = viewModel()) {
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf("") }

    val analysisResult by viewModel.analysisResult.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        OutlinedTextField(value = age, onValueChange = { age = it }, label = { Text("Age") })
        OutlinedTextField(value = gender, onValueChange = { gender = it }, label = { Text("Gender (0=Male, 1=Female)") })
        OutlinedTextField(value = height, onValueChange = { height = it }, label = { Text("Height (cm)") })
        OutlinedTextField(value = weight, onValueChange = { weight = it }, label = { Text("Weight (kg)") })
        OutlinedTextField(value = bmi, onValueChange = { bmi = it }, label = { Text("BMI") })

        Button(
            onClick = {
                viewModel.analyze(
                    age.toFloatOrNull() ?: 0f,
                    gender.toFloatOrNull() ?: 0f,
                    height.toFloatOrNull() ?: 0f,
                    weight.toFloatOrNull() ?: 0f,
                    bmi.toFloatOrNull() ?: 0f
                )
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Analisis")
        }

        Text(text = "Hasil Analisis: $analysisResult", style = MaterialTheme.typography.h6)
    }
}

@Preview
@Composable

fun HealthPreview(){
    Surface(modifier = Modifier.fillMaxSize()) {
        HealthScreen()
    }
}
