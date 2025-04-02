package org.wahyuheriyanto.nutricom.view

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.viewmodel.FoodViewModel
import org.wahyuheriyanto.nutricom.viewmodel.ScanViewModel

@Composable
fun ResultScreen(navController: NavController, viewModel: ScanViewModel
                 , viewModelTwo: FoodViewModel) {
    val foodInfo by viewModel.foodInfo.collectAsState()
    val foodViewModel = viewModelTwo


    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        ConstraintLayout {
            val startGuideline = createGuidelineFromStart(0.1f)
            val endGuideline = createGuidelineFromEnd(0.1f)
            val (title,content) = createRefs()

            Text(text = "Informasi Nilai Gizi",
                fontFamily = FontFamily(
                    Font(
                        resId = R.font.inter_semibold,
                        weight = FontWeight.SemiBold
                    )
                ),
                fontSize = 20.sp,
                color = Color(android.graphics.Color.parseColor("#000000")),
                modifier = Modifier.constrainAs(title){
                    top.linkTo(parent.top, margin = 20.dp)
                    start.linkTo(startGuideline)
                })

            Box(
                modifier = Modifier
                    .width(320.dp)
                    .height(460.dp)
                    .constrainAs(content) {
                        top.linkTo(title.bottom, margin = 15.dp)
                        start.linkTo(startGuideline)
                        end.linkTo(endGuideline)
                    }
                    .border(width = 1.dp, color = Color.Gray)
            ){
                ConstraintLayout {
                    val startGuidelineContent = createGuidelineFromStart(0.1f)
                    val endGuidelineContent = createGuidelineFromEnd(0.1f)
                    val topGuidelineContent = createGuidelineFromTop(0.05f)

                    val (name, calories, glukosa, fat, fatjenuh, natrium, kolesterol, valName, valCalories, valGlukosa,valFat, valFatJenuh,valNatrium,valKolesterol, button, button2) = createRefs()
                    val button3 = createRef()
                    //Nama produk
                    Text(text = "Nama Produk",
                        fontFamily = FontFamily(
                            Font(
                                resId = R.font.inter_semibold,
                                weight = FontWeight.SemiBold
                            )
                        ),
                        fontSize = 16.sp,
                        color = Color(android.graphics.Color.parseColor("#000000")),
                        modifier = Modifier.constrainAs(name){
                            top.linkTo(parent.top, margin = 25.dp)
                            start.linkTo(startGuideline)
                        }
                        )
                    //Kalori
                    Text(text = "Calories",
                        fontFamily = FontFamily(
                            Font(
                                resId = R.font.inter_medium,
                                weight = FontWeight.Medium
                            )
                        ),
                        fontSize = 16.sp,
                        color = Color(android.graphics.Color.parseColor("#000000")),
                        modifier = Modifier.constrainAs(calories){
                            top.linkTo(name.bottom, margin = 40.dp)
                            start.linkTo(startGuidelineContent)
                        }
                    )

                    //Glukosa
                    Text(text = "Glukosa",
                        fontFamily = FontFamily(
                            Font(
                                resId = R.font.inter_medium,
                                weight = FontWeight.Medium
                            )
                        ),
                        fontSize = 16.sp,
                        color = Color(android.graphics.Color.parseColor("#000000")),
                        modifier = Modifier.constrainAs(glukosa){
                            top.linkTo(calories.top)
                            start.linkTo(calories.end, margin = 100.dp)
                        }
                    )

                    //Lemak Total
                    Text(text = "Lemak Total",
                        fontFamily = FontFamily(
                            Font(
                                resId = R.font.inter_medium,
                                weight = FontWeight.Medium
                            )
                        ),
                        fontSize = 16.sp,
                        color = Color(android.graphics.Color.parseColor("#000000")),
                        modifier = Modifier.constrainAs(fat){
                            top.linkTo(calories.bottom, margin = 40.dp)
                            start.linkTo(startGuidelineContent)
                        }
                    )


                    //Lemak Jenuh
                    Text(text = "Lemak Jenuh",
                        fontFamily = FontFamily(
                            Font(
                                resId = R.font.inter_medium,
                                weight = FontWeight.Medium
                            )
                        ),
                        fontSize = 16.sp,
                        color = Color(android.graphics.Color.parseColor("#000000")),
                        modifier = Modifier.constrainAs(fatjenuh){
                            top.linkTo(glukosa.bottom, margin = 40.dp)
                            start.linkTo(glukosa.start)
                        }
                    )

                    //Natrium
                    Text(text = "Natrium",
                        fontFamily = FontFamily(
                            Font(
                                resId = R.font.inter_medium,
                                weight = FontWeight.Medium
                            )
                        ),
                        fontSize = 16.sp,
                        color = Color(android.graphics.Color.parseColor("#000000")),
                        modifier = Modifier.constrainAs(natrium){
                            top.linkTo(fat.bottom, margin = 40.dp)
                            start.linkTo(startGuidelineContent)
                        }
                    )

                    //Kolesterol
                    Text(text = "Kolesterol",
                        fontFamily = FontFamily(
                            Font(
                                resId = R.font.inter_medium,
                                weight = FontWeight.Medium
                            )
                        ),
                        fontSize = 16.sp,
                        color = Color(android.graphics.Color.parseColor("#000000")),
                        modifier = Modifier.constrainAs(kolesterol){
                            top.linkTo(fatjenuh.bottom, margin = 40.dp)
                            start.linkTo(fatjenuh.start)
                        }
                    )


                foodInfo?.let { response ->
                    response.product?.let { product ->
                    //Isi nama
                    Text(text = "${product.productName ?: "Tidak ditemukan"}",
                        fontFamily = FontFamily(
                            Font(
                                resId = R.font.inter_semibold,
                                weight = FontWeight.SemiBold
                            )
                        ),
                        fontSize = 16.sp,
                        color = Color(android.graphics.Color.parseColor("#000000")),
                        modifier = Modifier.constrainAs(valName){
                            top.linkTo(parent.top, margin = 25.dp)
                            start.linkTo(name.end, margin = 30.dp)
                        }
                    )
                    //Isi calories
                    Text(text = "${product.nutriments?.calories ?: "Tidak tersedia"} kkal",
                        fontFamily = FontFamily(
                            Font(
                                resId = R.font.inter_medium,
                                weight = FontWeight.Medium
                            )
                        ),
                        fontSize = 16.sp,
                        color = Color(android.graphics.Color.parseColor("#000000")),
                        modifier = Modifier.constrainAs(valCalories){
                            top.linkTo(calories.bottom, margin = 10.dp)
                            start.linkTo(startGuidelineContent)
                        }
                    )

                    //Isi gula
                    Text(text = "${product.nutriments?.sugars ?: "Tidak tersedia"} g",
                        fontFamily = FontFamily(
                            Font(
                                resId = R.font.inter_medium,
                                weight = FontWeight.Medium
                            )
                        ),
                        fontSize = 16.sp,
                        color = Color(android.graphics.Color.parseColor("#000000")),
                        modifier = Modifier.constrainAs(valGlukosa){
                            top.linkTo(glukosa.bottom, margin = 10.dp)
                            start.linkTo(glukosa.start)
                        }
                    )

                    //Isi lemak
                    Text(text = "${product.nutriments?.fat ?: "Tidak tersedia"} g",
                        fontFamily = FontFamily(
                            Font(
                                resId = R.font.inter_medium,
                                weight = FontWeight.Medium
                            )
                        ),
                        fontSize = 16.sp,
                        color = Color(android.graphics.Color.parseColor("#000000")),
                        modifier = Modifier.constrainAs(valFat){
                            top.linkTo(fat.bottom, margin = 10.dp)
                            start.linkTo(startGuidelineContent)
                        }
                    )

                    //Isi lemak jenuh
                    Text(text = "${product.nutriments?.saturatedFat ?: "Tidak tersedia"} g",
                        fontFamily = FontFamily(
                            Font(
                                resId = R.font.inter_medium,
                                weight = FontWeight.Medium
                            )
                        ),
                        fontSize = 16.sp,
                        color = Color(android.graphics.Color.parseColor("#000000")),
                        modifier = Modifier.constrainAs(valFatJenuh){
                            top.linkTo(fatjenuh.bottom, margin = 10.dp)
                            start.linkTo(fatjenuh.start)
                        }
                    )
                        //Isi Natrium
                        Text(text = "${product.nutriments?.salt ?: "Tidak tersedia"} g",
                            fontFamily = FontFamily(
                                Font(
                                    resId = R.font.inter_medium,
                                    weight = FontWeight.Medium
                                )
                            ),
                            fontSize = 16.sp,
                            color = Color(android.graphics.Color.parseColor("#000000")),
                            modifier = Modifier.constrainAs(valNatrium){
                                top.linkTo(natrium.bottom, margin = 10.dp)
                                start.linkTo(startGuidelineContent)
                            }
                        )

                        //Isi Kolesterol
                        Text(text = " g",
                            fontFamily = FontFamily(
                                Font(
                                    resId = R.font.inter_medium,
                                    weight = FontWeight.Medium
                                )
                            ),
                            fontSize = 16.sp,
                            color = Color(android.graphics.Color.parseColor("#000000")),
                            modifier = Modifier.constrainAs(valKolesterol){
                                top.linkTo(kolesterol.bottom, margin = 10.dp)
                                start.linkTo(kolesterol.start)
                            }
                        )
                    } ?: Text(text = "Produk tidak ditemukan", color = Color.Red)
                } ?: CircularProgressIndicator()

//
                    //Tombol scan ulang
                    Button(onClick = {
                        viewModel.clearFoodInfo()
                        navController.navigate("scanScreen") {
                            popUpTo("scanScreen") { inclusive = true }
                        }
                    },
                        modifier = Modifier.constrainAs(button){
                            top.linkTo(valKolesterol.bottom, margin = 30.dp)
                            start.linkTo(startGuidelineContent)
                            end.linkTo(endGuidelineContent)
                        }
                        , colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF00AA16),
                            contentColor = Color.White,
                            disabledBackgroundColor = Color.Gray,
                            disabledContentColor = Color.LightGray
                        )) {
                        Text(text = "Scan Lagi")
                    }

                    //Tombol jangan simpan
                    Button(onClick = {
                        viewModel.clearFoodInfo()

                        navController.navigate("market") {
                            popUpTo("market") { inclusive = true }
                        }
                    },
                        modifier = Modifier.constrainAs(button2){
                            top.linkTo(button.bottom, margin = 15.dp)
                            start.linkTo(startGuidelineContent, margin = 3.dp)
                        }, colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF00AA16),
                            contentColor = Color.White,
                            disabledBackgroundColor = Color.Gray,
                            disabledContentColor = Color.LightGray
                        )) {
                        Text(text = "Jangan Simpan")
                    }

                    //Tombol simpan
                    Button(onClick = {
                        foodInfo?.product?.let { product ->
                            foodViewModel.addFoodEntry(
                                barcode = product.code ?: "Tidak tersedia",
                                name = product.productName ?: "Tidak ditemukan",
                                calories = product.nutriments?.calories ?: 0.0,
                                sugars = product.nutriments?.sugars ?:0.0,
                                cholesterol = product.nutriments?.cholesterol ?: 0.0,
                                fat = product.nutriments?.fat ?: 0.0,
                                salt = product.nutriments?.salt ?: 0.0,
                                saturatedFat = product.nutriments?.saturatedFat ?: 0.0
                            )
                        }
                        viewModel.clearFoodInfo()
                        navController.navigate("market") {
                            popUpTo("market") { inclusive = true }
                        }
                    },
                        modifier = Modifier.constrainAs(button3){
                            top.linkTo(button.bottom, margin = 15.dp)
                            end.linkTo(endGuidelineContent, margin = 3.dp)
                        }
                        , colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color(0xFF00AA16),
                            contentColor = Color.White,
                            disabledBackgroundColor = Color.Gray,
                            disabledContentColor = Color.LightGray
                        )) {
                        Text(text = "Simpan")
                    }
                }

            }
        }

    }
}

@Preview
@Composable

fun PreviewView(){
    Surface(modifier = Modifier.fillMaxSize()) {
        ResultScreen(navController = rememberNavController(), viewModel = ScanViewModel(), viewModelTwo = FoodViewModel())
    }
}
