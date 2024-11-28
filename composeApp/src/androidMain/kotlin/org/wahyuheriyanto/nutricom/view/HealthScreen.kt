package org.wahyuheriyanto.nutricom.view


import android.app.Application
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.ComponentActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.wahyuheriyanto.nutricom.viewmodel.HealthViewModel


@Composable
fun HealthScreen(viewModel: HealthViewModel = viewModel()) {
    var gender by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var hypertension by remember { mutableStateOf("") }
    var heartDisease by remember { mutableStateOf("") }
    var smokingHistory by remember { mutableStateOf("") }
    var bmi by remember { mutableStateOf("") }
    var hba1c by remember { mutableStateOf("") }
    var glucoseLevel by remember { mutableStateOf("") }
    val result by viewModel.analysisResult.collectAsState()

    LazyColumn(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        item {
            TextField(
                value = gender,
                onValueChange = { gender = it },
                label = { Text("Gender (0 = Female, 1 = Male)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            TextField(
                value = age,
                onValueChange = { age = it },
                label = { Text("Age") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            TextField(
                value = hypertension,
                onValueChange = { hypertension = it },
                label = { Text("Hypertension (0 or 1)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            TextField(
                value = heartDisease,
                onValueChange = { heartDisease = it },
                label = { Text("Heart Disease (0 or 1)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            TextField(
                value = smokingHistory,
                onValueChange = { smokingHistory = it },
                label = { Text("Smoking History (0-4)") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            TextField(
                value = bmi,
                onValueChange = { bmi = it },
                label = { Text("BMI") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            TextField(
                value = hba1c,
                onValueChange = { hba1c = it },
                label = { Text("HbA1c Level") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            TextField(
                value = glucoseLevel,
                onValueChange = { glucoseLevel = it },
                label = { Text("Blood Glucose Level") },
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Button(
                onClick = {
                    viewModel.analyze(
                        gender.toFloatOrNull() ?: 0f,
                        age.toFloatOrNull() ?: 0f,
                        hypertension.toFloatOrNull() ?: 0f,
                        heartDisease.toFloatOrNull() ?: 0f,
                        smokingHistory.toFloatOrNull() ?: 0f,
                        bmi.toFloatOrNull() ?: 0f,
                        hba1c.toFloatOrNull() ?: 0f,
                        glucoseLevel.toFloatOrNull() ?: 0f
                    )
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Analyze")
            }
        }
        item {
            if (result.isNotEmpty()) {
                Text(
                    "Result: $result",
                    style = MaterialTheme.typography.h6,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}

@Preview
@Composable
fun HealthPreview() {
    Surface(modifier = Modifier.fillMaxSize()) {
        HealthScreen()
    }
}

