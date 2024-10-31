package org.wahyuheriyanto.nutricom.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ActivityScreen() {
    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        Text(text = "Selamat datang di halaman Activity!")
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Ini adalah deskripsi dari aplikasi.")
    }
}