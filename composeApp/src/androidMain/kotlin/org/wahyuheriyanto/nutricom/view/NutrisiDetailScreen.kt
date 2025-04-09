package org.wahyuheriyanto.nutricom.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.view.components.ConsumtionItem
import org.wahyuheriyanto.nutricom.view.components.ConsumtionLoading
import org.wahyuheriyanto.nutricom.view.components.ScreeningItem
import org.wahyuheriyanto.nutricom.view.components.ScreeningLoading
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DataViewModel
import org.wahyuheriyanto.nutricom.viewmodel.LoginState
import org.wahyuheriyanto.nutricom.viewmodel.deleteConsumtion
import org.wahyuheriyanto.nutricom.viewmodel.fetchConsumtion
import org.wahyuheriyanto.nutricom.viewmodel.fetchScreningResult

@Composable
fun NutrisiDetailScreen(navController: NavController, viewModel: AuthViewModel, viewModelTwo: DataViewModel){
    val scrollState = rememberScrollState()
    val loginState: LoginState by viewModel.loginState.collectAsState()
    val consumeResults by viewModelTwo.consumtion.collectAsState()

    fetchConsumtion(viewModelTwo = viewModelTwo)

    LaunchedEffect(Unit) {
        fetchConsumtion(viewModelTwo)
    }

    Column (modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .background(Color(android.graphics.Color.parseColor("#F4F4F4")))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp, start = 30.dp, end = 30.dp)
        ){
            Text(text = "Konsumsi Nutrisi Harian",
                fontFamily = FontFamily(
                    Font(
                        resId = R.font.inter_semibold,
                        weight = FontWeight.SemiBold
                    )
                ),
                fontSize = 16.sp,
                color = Color(android.graphics.Color.parseColor("#000000")))
            Spacer(modifier = Modifier.height(15.dp))

            when (loginState) {
                is LoginState.Loading -> {
                    Log.e("tesscreen","masih loading")
                    repeat(consumeResults.size) {
                        ConsumtionLoading()
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                is LoginState.Success -> {
                    Log.e("tesscreen","sudah sukses")
                    consumeResults.forEach { consum ->
                        ConsumtionItem(consumtion = consum) {
                            deleteConsumtion(viewModelTwo = viewModelTwo,consum.name)
                        }
                        Log.e("tesscreen","hasil : $consum")
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                else -> {
                    repeat(consumeResults.size) {
                        ConsumtionLoading()
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }




        }
    }

}