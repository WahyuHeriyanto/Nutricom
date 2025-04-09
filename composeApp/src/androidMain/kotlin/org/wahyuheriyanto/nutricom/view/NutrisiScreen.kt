package org.wahyuheriyanto.nutricom.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
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
import org.jetbrains.compose.resources.imageResource
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.view.components.CustomCircularProgressBar
import org.wahyuheriyanto.nutricom.view.components.CustomProgressBar
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DataViewModel
import org.wahyuheriyanto.nutricom.viewmodel.FoodViewModel
import org.wahyuheriyanto.nutricom.viewmodel.LoginState
import org.wahyuheriyanto.nutricom.viewmodel.fetchNutricions
import org.wahyuheriyanto.nutricom.viewmodel.fetchScreningResult

@Composable
fun NutrisiScreen(viewModel: AuthViewModel, navController: NavController, viewModelTwo: DataViewModel,
                  viewModelThree: FoodViewModel) {
    val loginState: LoginState by viewModel.loginState.collectAsState()
    val scrollState = rememberScrollState()
    val calories by viewModelTwo.calories.collectAsState()
    val glukosa by viewModelTwo.sugars.collectAsState()
    val fat by viewModelTwo.fat.collectAsState()
    val saturatedFat by viewModelTwo.saturatedFat.collectAsState()
    val natrium by viewModelTwo.salt.collectAsState()
    val cholesterol by viewModelTwo.cholesterol.collectAsState()

    val maxIntake by viewModelThree.maxDailyIntake.collectAsState()

    fetchNutricions(viewModelTwo = viewModelTwo)
    viewModelThree.calculateMaxDailyIntake()
    FoodViewModel().calculateNutricion()

    LaunchedEffect(Unit) {
        fetchNutricions(viewModelTwo)
        viewModelThree.calculateMaxDailyIntake()
        FoodViewModel().calculateNutricion()
    }


    Column (modifier = Modifier
        .fillMaxSize()
        .verticalScroll(scrollState)
        .background(Color(android.graphics.Color.parseColor("#F4F4F4")))
        .padding(vertical = 10.dp)
    ) {
        ConstraintLayout {
            val startGuideline = createGuidelineFromStart(0.1f)
            val endGuideline = createGuidelineFromEnd(0.1f)
            val (picture, button, content) = createRefs()
            
            Image( painter = painterResource(id = R.drawable.scan),
                contentDescription = "",
                modifier = Modifier
                    .width(320.dp)
                    .height(180.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .constrainAs(picture) {
                        top.linkTo(parent.top)
                        start.linkTo(startGuideline)
                        end.linkTo(endGuideline)
                    }
            )
            Box(modifier = Modifier
                .width(140.dp)
                .height(40.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(color = Color(0xFF00AA16))
                .constrainAs(button) {
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                    top.linkTo(picture.bottom, margin = 20.dp)
                }
                ,contentAlignment = Alignment.Center){

                Row (
                    Modifier.clickable {
                        navController.navigate("scanScreen")
                    }
                ){
                    Image(painter = painterResource(id = R.drawable.scan_icon), contentDescription = ""
                    , modifier = Modifier.size(30.dp))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(text = "Scan", color = Color.White, fontSize = 25.sp, fontWeight = FontWeight.Bold)
                }
            }

            Box(modifier = Modifier
                .width(320.dp)
                .height(520.dp)
                .clip(RoundedCornerShape(20.dp))
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 20.dp)
                .constrainAs(content) {
                    top.linkTo(button.bottom, margin = 20.dp)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                }
                .clickable {
                    navController.navigate("nutrisiDetail")
                }
            ){
                Column {
                    Row {
                        CustomCircularProgressBar(current = 150, max = 1800)
                        Spacer(modifier = Modifier.width(15.dp))
                        Column {
                            Text(text = "Kebutuhan Kalori Harian",
                                fontFamily = FontFamily(
                                Font(
                                    resId = R.font.inter_medium,
                                    weight = FontWeight.Medium
                                )
                            ),
                                fontSize = 15.sp,
                                color = Color(android.graphics.Color.parseColor("#737373")))
                            when (loginState) {
                                is LoginState.Loading -> { }
                                is LoginState.Success -> {
                                    val calorie = calories.toString()
                                    val maxCalories = maxIntake["kalori"]?.toInt()
                                    androidx.compose.material.Text(
                                        text = "$calorie / $maxCalories kkal",
                                        fontFamily = FontFamily(
                                            Font(
                                                resId = R.font.inter_medium,
                                                weight = FontWeight.Medium
                                            )
                                        ),
                                        fontSize = 15.sp,
                                        color = Color(android.graphics.Color.parseColor("#00AA16"))
                                    )

                                }
                                else -> { }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(15.dp))
                    Text(text = "Batas Kecukupan Nutrisi",
                        fontFamily = FontFamily(
                        Font(
                            resId = R.font.inter_medium,
                            weight = FontWeight.Medium
                        )
                    ),
                        fontSize = 16.sp,
                        color = Color(android.graphics.Color.parseColor("#000000")))

                    //Isi nutrisi
                    Spacer(modifier = Modifier.height(20.dp))
                    when (loginState) {
                        is LoginState.Loading -> { }
                        is LoginState.Success -> {
                            val sugar = glukosa.toInt()
                            Row {
                                Image(painter = painterResource(id = R.drawable.sugar), contentDescription = "",
                                    modifier = Modifier.size(60.dp))
                                Spacer(modifier = Modifier.width(15.dp))
                                CustomProgressBar(labelName = "Glukosa", current = sugar, max = maxIntake["glukosa"])
                            }

                        }
                        else -> { }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    when (loginState) {
                        is LoginState.Loading -> { }
                        is LoginState.Success -> {
                            val fatValue = fat.toInt()
                            Row {
                                Image(painter = painterResource(id = R.drawable.green_box_signin), contentDescription = "",
                                    modifier = Modifier.size(60.dp))
                                Spacer(modifier = Modifier.width(15.dp))
                                CustomProgressBar(labelName = "Lemak", current = fatValue, max = maxIntake["lemak"])
                            }

                        }
                        else -> { }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    when (loginState) {
                        is LoginState.Loading -> { }
                        is LoginState.Success -> {
                            val satFatValue = saturatedFat.toInt()
                            Row {
                                Image(painter = painterResource(id = R.drawable.jenuh), contentDescription = "",
                                    modifier = Modifier.size(60.dp))
                                Spacer(modifier = Modifier.width(15.dp))
                                CustomProgressBar(labelName = "Lemak Jenuh", current = satFatValue, max = maxIntake["lemakJenuh"])
                            }
                        }
                        else -> { }
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                    when (loginState) {
                        is LoginState.Loading -> { }
                        is LoginState.Success -> {
                            val salt = natrium.toInt()
                            Row {
                                Image(painter = painterResource(id = R.drawable.garam), contentDescription = "",
                                    modifier = Modifier.size(60.dp))
                                Spacer(modifier = Modifier.width(15.dp))
                                CustomProgressBar(labelName = "Natrium", current = salt, max = maxIntake["natrium"])
                            }
                        }
                        else -> { }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    when (loginState) {
                        is LoginState.Loading -> { }
                        is LoginState.Success -> {
                            val chole = cholesterol.toInt()
                            Row {
                                Image(painter = painterResource(id = R.drawable.green_box_signin), contentDescription = "",
                                    modifier = Modifier.size(60.dp))
                                Spacer(modifier = Modifier.width(15.dp))
                                CustomProgressBar(labelName = "Kolesterol", current = chole, max = maxIntake["kolesterol"])
                            }
                        }
                        else -> { }
                    }

                }
            }
        }
    }
}