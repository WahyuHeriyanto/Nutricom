package org.wahyuheriyanto.nutricom.view

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
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
import org.wahyuheriyanto.nutricom.view.components.RecListActive
import org.wahyuheriyanto.nutricom.view.components.RecommenderActive
import org.wahyuheriyanto.nutricom.view.components.ScreeningItem
import org.wahyuheriyanto.nutricom.view.components.ScreeningLoading
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DataViewModel
import org.wahyuheriyanto.nutricom.viewmodel.FoodViewModel
import org.wahyuheriyanto.nutricom.viewmodel.LoginState
import org.wahyuheriyanto.nutricom.viewmodel.fetchNutricions
import org.wahyuheriyanto.nutricom.viewmodel.fetchRecommender

@Composable
fun RecommendationScreen(navController: NavController, viewModel: AuthViewModel, viewModelTwo: DataViewModel){
    val loginState: LoginState by viewModel.loginState.collectAsState()
    val recommenderResults by viewModelTwo.recommenders.collectAsState()
    val recommendation by viewModelTwo.recommenders.collectAsState()
    val scrollState = rememberScrollState()

    LaunchedEffect(Unit) {
        fetchRecommender(viewModelTwo)
    }

    Column (modifier = Modifier
        .fillMaxSize()
        .padding(top = 25.dp, start = 30.dp, end = 30.dp)
        .verticalScroll(scrollState)
        .background(Color(android.graphics.Color.parseColor("#F4F4F4")))
    ) {
        Text(
            text = "Rekomendasi Kesehatan",
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
        Text(
            text = "Aktivitas yang disarankan untuk anda",
            fontFamily = FontFamily(
                Font(
                    resId = R.font.inter_medium,
                    weight = FontWeight.Medium
                )
            ),
            fontSize = 16.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(10.dp))

        when (loginState) {
            is LoginState.Loading -> {
                Log.e("tesscreen","masih loading")
                repeat(recommenderResults.size) {
                    ScreeningLoading()
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            is LoginState.Success -> {
                Log.e("tesscreen","sudah sukses")
                recommenderResults.forEach { recommender->
                    Log.e("aduh","$recommender")
                    RecommenderActive(recItem = recommender)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
            else -> {
                repeat(recommenderResults.size) {
                    ScreeningLoading()
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }

    }
}