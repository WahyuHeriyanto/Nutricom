package org.wahyuheriyanto.nutricom.view.screens

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.view.components.ArticleItem
import org.wahyuheriyanto.nutricom.view.components.ArticleLoading
import org.wahyuheriyanto.nutricom.view.components.RecListActive
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DataViewModel
import org.wahyuheriyanto.nutricom.viewmodel.LoginState
import org.wahyuheriyanto.nutricom.viewmodel.fetchLastestArticle
import org.wahyuheriyanto.nutricom.viewmodel.fetchRecommender
import org.wahyuheriyanto.nutricom.viewmodel.performData


@Composable
fun HomeScreen(viewModel: AuthViewModel, viewModelTwo: DataViewModel, navController: NavController) {
    val loginState: LoginState by viewModel.loginState.collectAsState()
    val name by viewModel.userName.collectAsState()
    val newUserStatus by viewModel.statusLogin.collectAsState()
    val bmi by viewModelTwo.bmi.collectAsState()
    val weightVal by viewModelTwo.height_value.collectAsState()
    val heightVal by viewModelTwo.weight_value.collectAsState()
    val recommendation by viewModelTwo.recommenders.collectAsState()
    val articles by viewModelTwo.articles.collectAsState()
    val scrollState = rememberScrollState()
    val columnScrollState = rememberScrollState()
    var showDialog by remember { mutableStateOf(false) }
    performData(viewModel = viewModel, viewModelTwo = viewModelTwo)

    fetchLastestArticle(viewModelTwo = viewModelTwo)
    fetchRecommender(viewModelTwo = viewModelTwo)

    LaunchedEffect(Unit) {
        fetchLastestArticle(viewModelTwo)
        fetchRecommender(viewModelTwo)
    }
    LaunchedEffect(newUserStatus) {
        if (newUserStatus) {
            delay(2000) // Delay 1.5 detik
            showDialog = true
        }
    }

    //Scroll Box for Content
    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(android.graphics.Color.parseColor("#F4F4F4")))
    ) {
        ConstraintLayout {

            val startGuideline = createGuidelineFromStart(0.1f)
            val endGuideline = createGuidelineFromEnd(0.1f)
            val (titleBox, indicatorBox, recBox, article, articleList) = createRefs()

            Box(modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .background(Color(android.graphics.Color.parseColor("#23BF4C")))
                .constrainAs(titleBox) {}
            ) {
                Row(modifier = Modifier.padding(start = 40.dp, top = 20.dp)) {
                    Text(
                        text = "Halo, ",
                        fontFamily = FontFamily(
                            Font(
                                resId = R.font.inter_medium,
                                weight = FontWeight.Medium
                            )
                        ),
                        fontSize = 32.sp,
                        color = Color.White
                    )
                    when (loginState) {
                        is LoginState.Loading -> { }
                        is LoginState.Success -> {
                            val names = name
                            Text(
                                text = names,
                                fontFamily = FontFamily(
                                    Font(
                                        resId = R.font.inter_medium,
                                        weight = FontWeight.Medium
                                    )
                                ),
                                fontSize = 32.sp,
                                color = Color.White
                            )
                        }
                        else -> { }
                    }
                }
            }

            Box(modifier = Modifier
                .width(320.dp)
                .height(100.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .constrainAs(indicatorBox) {
                    top.linkTo(titleBox.bottom, margin = (-50).dp)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                },
                Alignment.Center
            ){
                Row (modifier = Modifier
                    .padding(horizontal = 5.dp)
                    .width(280.dp),
                    horizontalArrangement = Arrangement.SpaceBetween, // Elemen kiri dan kanan berjauhan
                    verticalAlignment = Alignment.CenterVertically){
                    Column (horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "Body Mass Index",
                            fontFamily = FontFamily(
                                Font(
                                    resId = R.font.inter_medium,
                                    weight = FontWeight.Medium
                                )
                            ),
                            fontSize = 11.sp,
                            color = Color(android.graphics.Color.parseColor("#737373")))
                        Spacer(modifier = Modifier.height(5.dp))
                        Row (verticalAlignment = Alignment.Bottom){
                            when (loginState) {
                                is LoginState.Loading -> { }
                                is LoginState.Success -> {
                                    val bmiValue = bmi.toString()
                                    Text(text = bmiValue,
                                        fontFamily = FontFamily(
                                            Font(
                                                resId = R.font.inter_medium,
                                                weight = FontWeight.Medium
                                            )
                                        ),
                                        fontSize = 24.sp,
                                        color = Color(android.graphics.Color.parseColor("#00AA16"))
                                    )
                                }
                                else -> { }
                            }

                            Text(text = " Normal",
                                fontFamily = FontFamily(
                                    Font(
                                        resId = R.font.inter_medium,
                                        weight = FontWeight.Medium
                                    )
                                ),
                                fontSize = 16.sp,
                                color = Color(android.graphics.Color.parseColor("#00AA16")))
                        }
                    }
                    Column {
                        Text(text = "Berat Badan",
                            fontFamily = FontFamily(
                                Font(
                                    resId = R.font.inter_medium,
                                    weight = FontWeight.Medium
                                )
                            ),
                            fontSize = 11.sp,
                            color = Color(android.graphics.Color.parseColor("#737373")))
                        Spacer(modifier = Modifier.height(5.dp))
                        Row (verticalAlignment = Alignment.Bottom){

                            when (loginState) {
                                is LoginState.Loading -> { }
                                is LoginState.Success -> {
                                    val weights = weightVal.toString()
                                    Text(text = weights,
                                        fontFamily = FontFamily(
                                            Font(
                                                resId = R.font.inter_medium,
                                                weight = FontWeight.Medium
                                            )
                                        ),
                                        fontSize = 24.sp,
                                        color = Color(android.graphics.Color.parseColor("#00AA16"))
                                    )
                                }
                                else -> { }
                            }

                            Text(text = " Kg",
                                fontFamily = FontFamily(
                                    Font(
                                        resId = R.font.inter_medium,
                                        weight = FontWeight.Medium
                                    )
                                ),
                                fontSize = 16.sp,
                                color = Color(android.graphics.Color.parseColor("#00AA16")))
                        }
                    }
                    Column {
                        Text(text = "Tinggi Badan",
                            fontFamily = FontFamily(
                                Font(
                                    resId = R.font.inter_medium,
                                    weight = FontWeight.Medium
                                )
                            ),
                            fontSize = 11.sp,
                            color = Color(android.graphics.Color.parseColor("#737373")))
                        Spacer(modifier = Modifier.height(5.dp))
                        Row (verticalAlignment = Alignment.Bottom){

                            when (loginState) {
                                is LoginState.Loading -> { }
                                is LoginState.Success -> {
                                    val heights = heightVal.toString()
                                    Text(text = heights,
                                        fontFamily = FontFamily(
                                            Font(
                                                resId = R.font.inter_medium,
                                                weight = FontWeight.Medium
                                            )
                                        ),
                                        fontSize = 24.sp,
                                        color = Color(android.graphics.Color.parseColor("#00AA16")))

                                }
                                else -> { }
                            }
                            Text(text = " Cm",
                                fontFamily = FontFamily(
                                    Font(
                                        resId = R.font.inter_medium,
                                        weight = FontWeight.Medium
                                    )
                                ),
                                fontSize = 16.sp,
                                color = Color(android.graphics.Color.parseColor("#00AA16")))
                        }
                    }
                }
            }

            Box(modifier = Modifier
                .width(320.dp)
                .height(260.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .verticalScroll(columnScrollState)
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .constrainAs(recBox) {
                    top.linkTo(indicatorBox.bottom, margin = 20.dp)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                }
                .clickable {
                    navController.navigate("recommendationList")
                }
            ) {
                Column{
                    Text(text = "Rekomendasi Kesehatan",
                        fontFamily = FontFamily(
                            Font(
                                resId = R.font.inter_medium,
                                weight = FontWeight.Medium
                            )
                        ),
                        fontSize = 16.sp,
                        color = Color(android.graphics.Color.parseColor("#000000")))
                    Spacer(modifier = Modifier.height(15.dp))
                    recommendation.forEach { rec ->
                        RecListActive(recItem = rec, viewModel = viewModelTwo)
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }

            Row(
                modifier = Modifier
                    .width(320.dp)
                    .constrainAs(article) {
                        start.linkTo(startGuideline)
                        end.linkTo(endGuideline)
                        top.linkTo(recBox.bottom, margin = 30.dp)
                    },
                horizontalArrangement = Arrangement.SpaceBetween, // Elemen kiri dan kanan berjauhan
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Artikel Kesehatan",
                    fontFamily = FontFamily(
                        Font(
                            resId = R.font.inter_semibold,
                            weight = FontWeight.SemiBold
                        )
                    ),
                    fontSize = 18.sp
                )

                Text(
                    text = "Lihat Semua",
                    fontFamily = FontFamily(
                        Font(
                            resId = R.font.inter_semibold,
                            weight = FontWeight.SemiBold
                        )
                    ),
                    fontSize = 14.sp,
                    color = Color(android.graphics.Color.parseColor("#00AA16"))
                    , modifier = Modifier.clickable {
                        navController.navigate("artikel")
                    }
                )
            }

            Column(modifier = Modifier
                .width(320.dp)
                .height(520.dp)
                .background(color = Color.Transparent)
                .constrainAs(articleList) {
                    top.linkTo(article.bottom, margin = 10.dp)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                }
            ) {
                when (loginState) {
                    is LoginState.Loading -> {
                        repeat(articles.size) {
                            ArticleLoading()
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    is LoginState.Success -> {
                        articles.forEach { article ->
                            ArticleItem(article = article, navController = navController)
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                    else -> {
                        repeat(articles.size) {
                            ArticleLoading()
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }

            if (showDialog) {
                AlertDialog(
                    onDismissRequest = {
                        showDialog = false
                    },
                    title = { Text("Selamat datang $name") },
                    text = { Text("Sebagai pengguna baru, kita akan melakukan skrining awal terlebih dahulu") },
                    confirmButton = {
                        Button(onClick = {
                            navController.navigate("newScreening")
                            showDialog = false
                        }
                        ) {
                            Text("Lanjut")
                        }
                    }
                )
            }

        }
    }
}

//@Preview
//@Composable
//
//fun PreviewScreen(){
//    Surface (modifier = Modifier.fillMaxSize()){
//        HomeScreen(viewModel = AuthViewModel(), viewModelTwo = DataViewModel())
//    }
//}