package org.wahyuheriyanto.nutricom.view

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DataViewModel
import org.wahyuheriyanto.nutricom.viewmodel.LoginState
import org.wahyuheriyanto.nutricom.viewmodel.performData


@Composable
fun HomeScreen(viewModel: AuthViewModel, viewModelTwo: DataViewModel) {
    val loginState by viewModel.loginState.collectAsState()
    val name by viewModel.userName.collectAsState()
    val wv by viewModelTwo.weight.collectAsState()
    val hv by viewModelTwo.height.collectAsState()
    val cv by viewModelTwo.calorie.collectAsState()
    val bv by viewModelTwo.bmi.collectAsState()

    performData(viewModel = viewModel, viewModelTwo = viewModelTwo)

    Column(modifier = Modifier
        .fillMaxSize()
    ) {
        when (loginState) {
            is LoginState.Loading -> {

            }
            is LoginState.Success -> {
                val names = name
                Text(text = "Selamat datang di halaman Home! $names")
                Text(text = "Weight : $wv")
                Text(text = "Height : $hv")
                Text(text = "Calories : $cv")
                Text(text = "Bmi : $bv")
            }
            else -> {
                // Idle state
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Ini adalah deskripsi dari aplikasi.")
    }
}

