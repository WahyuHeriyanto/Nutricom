package org.wahyuheriyanto.nutricom.view

import android.util.Log
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.view.components.ScreeningItem
import org.wahyuheriyanto.nutricom.view.components.ScreeningLoading
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DataViewModel
import org.wahyuheriyanto.nutricom.viewmodel.LoginState
import org.wahyuheriyanto.nutricom.viewmodel.fetchScreningResult

@Composable
fun ScreeningScreen(navController: NavController, viewModel: AuthViewModel, viewModelTwo : DataViewModel){
    val loginState: LoginState by viewModel.loginState.collectAsState()
    val scrollState = rememberScrollState()
    val screeningResults by viewModelTwo.screeningResults.collectAsState()

    fetchScreningResult(viewModelTwo = viewModelTwo)

    LaunchedEffect(Unit) {
        fetchScreningResult(viewModelTwo)
    }

    Column (modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .background(Color(android.graphics.Color.parseColor("#F4F4F4")))
    ) {
        ConstraintLayout {
            val startGuideline = createGuidelineFromStart(0.1f)
            val endGuideline = createGuidelineFromEnd(0.1f)
            val (features, history) = createRefs()

            Row (modifier = Modifier
                .fillMaxWidth()
                .padding(top = 25.dp, start = 30.dp, end = 30.dp)
                .constrainAs(features) {
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ){
                Box(modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = Color.White)
                    ,contentAlignment = Alignment.Center
                ){
                    Column (verticalArrangement = Arrangement.Center, // Menengahkan elemen secara vertikal
                        horizontalAlignment = Alignment.CenterHorizontally // Menengahkan elemen secara horizontal
                                ){
                        Image(painter = painterResource(id = R.drawable.health_icon), contentDescription = "",
                            modifier = Modifier.size(35.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Umum",
                            fontFamily = FontFamily(
                                Font(
                                    resId = R.font.inter_medium,
                                    weight = FontWeight.Medium
                                )
                            ),
                            fontSize = 12.sp,
                            color = Color(android.graphics.Color.parseColor("#000000"))
                        )
                    }
                }

                Box(modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = Color.White)
                    .clickable {
                        navController.navigate("diabetesScreen")
                    }
                    ,contentAlignment = Alignment.Center
                ){
                    Column (
                        verticalArrangement = Arrangement.Center, // Menengahkan elemen secara vertikal
                        horizontalAlignment = Alignment.CenterHorizontally // Menengahkan elemen secara horizontal
                    ){
                        Image(painter = painterResource(id = R.drawable.diabetes_icon), contentDescription = "",
                            modifier = Modifier.size(35.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Diabetes",
                            fontFamily = FontFamily(
                                Font(
                                    resId = R.font.inter_medium,
                                    weight = FontWeight.Medium
                                )
                            ),
                            fontSize = 12.sp,
                            color = Color(android.graphics.Color.parseColor("#000000"))
                        )
                    }
                }

                Box(modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(color = Color.White)
                    .clickable {
                        navController.navigate("cardioScreen")
                    }
                    ,contentAlignment = Alignment.Center
                ){
                    Column (
                        verticalArrangement = Arrangement.Center, // Menengahkan elemen secara vertikal
                        horizontalAlignment = Alignment.CenterHorizontally // Menengahkan elemen secara horizontal
                    ){
                        Image(painter = painterResource(id = R.drawable.kardio_icon), contentDescription = "",
                            modifier = Modifier.size(35.dp))
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(text = "Kardio",
                            fontFamily = FontFamily(
                                Font(
                                    resId = R.font.inter_medium,
                                    weight = FontWeight.Medium
                                )
                            ),
                            fontSize = 12.sp,
                            color = Color(android.graphics.Color.parseColor("#000000"))
                        )
                    }
                }
            }

            Column(
                modifier = Modifier.fillMaxWidth()
                .padding(top = 25.dp, start = 30.dp, end = 30.dp)
                .constrainAs(history) {
                    top.linkTo(features.bottom)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                },
            ){
                Text(text = "Riwayat Screening",
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
                        repeat(screeningResults.size) {
                            ScreeningLoading()
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    is LoginState.Success -> {
                        Log.e("tesscreen","sudah sukses")
                        screeningResults.forEach { screening ->
                            ScreeningItem(screening = screening)
                            Log.e("tesscreen","hasil : $screening")
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    else -> {
                        repeat(screeningResults.size) {
                            ScreeningLoading()
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }



                }
            }
        }
    }