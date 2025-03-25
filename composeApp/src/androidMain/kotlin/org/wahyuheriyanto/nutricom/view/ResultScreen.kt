package org.wahyuheriyanto.nutricom.view

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.wahyuheriyanto.nutricom.viewmodel.ScanViewModel

@Composable
fun ResultScreen(navController: NavController, viewModel: ScanViewModel) {
    val foodInfo by viewModel.foodInfo.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(text = "Hasil Scan", fontSize = 20.sp, fontWeight = FontWeight.Bold)

        Spacer(modifier = Modifier.height(16.dp))

        foodInfo?.let { response ->
            response.product?.let { product ->
                Text(text = "Nama Produk: ${product.productName ?: "Tidak ditemukan"}")
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Kalori: ${product.nutriments?.calories ?: "Tidak tersedia"} kkal")
                Text(text = "Protein: ${product.nutriments?.proteins ?: "Tidak tersedia"} g")
                Text(text = "Lemak: ${product.nutriments?.fat ?: "Tidak tersedia"} g")
                Text(text = "Karbohidrat: ${product.nutriments?.carbohydrates ?: "Tidak tersedia"} g")
            } ?: Text(text = "Produk tidak ditemukan", color = Color.Red)
        } ?: CircularProgressIndicator()

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            navController.navigate("market") {
                popUpTo("market") { inclusive = true }
            }
        }) {
            Text(text = "Scan Lagi")
        }
    }
}
