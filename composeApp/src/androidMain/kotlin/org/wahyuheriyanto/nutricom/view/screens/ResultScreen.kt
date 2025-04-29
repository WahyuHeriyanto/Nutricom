package org.wahyuheriyanto.nutricom.view.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        horizontalAlignment = Alignment.CenterHorizontally
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
                    end.linkTo(endGuideline)
                })

            Column(modifier = Modifier
                .width(320.dp)
                .height(460.dp)
                .constrainAs(content) {
                    top.linkTo(title.bottom, margin = 15.dp)
                    start.linkTo(startGuideline)
                    end.linkTo(endGuideline)
                }
                .border(width = 1.dp, color = Color.Gray))
            {

                foodInfo?.let { response ->
                    response.product?.let { product ->
                        Box(modifier = Modifier
                            .width(320.dp)
                            .height(50.dp)
                            .background(color = Color.LightGray)
                            .padding(10.dp)){
                            Text(text = "Nama produk : ${product.productName ?: "Nama produk : Tidak ditemukan"}",
                        fontFamily = FontFamily(
                            Font(
                                resId = R.font.inter_semibold,
                                weight = FontWeight.SemiBold
                            )
                        ),
                        fontSize = 16.sp,
                        color = Color(android.graphics.Color.parseColor("#000000"))
                    )
                        }
                        Row (modifier = Modifier
                            .width(320.dp)
                            .height(270.dp)){
                            Column(horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(25.dp)) {
                                //Kalori
                                Text(text = "Total Kalori",
                                    fontFamily = FontFamily(
                                        Font(
                                            resId = R.font.inter_medium,
                                            weight = FontWeight.Medium
                                        )
                                    ),
                                    fontSize = 16.sp,
                                    color = Color(android.graphics.Color.parseColor("#000000"))
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "${product.nutriments?.calories ?: "Tidak tersedia"} kkal",
                                fontFamily = FontFamily(
                                    Font(
                                        resId = R.font.inter_medium,
                                        weight = FontWeight.Medium
                                    )
                                ),
                                fontSize = 16.sp,
                                color = Color(android.graphics.Color.parseColor("#000000"))
                                )
                                Spacer(modifier = Modifier.height(20.dp))

                                //Total Lemak
                                Text(text = "Total Lemak",
                                    fontFamily = FontFamily(
                                        Font(
                                            resId = R.font.inter_medium,
                                            weight = FontWeight.Medium
                                        )
                                    ),
                                    fontSize = 16.sp,
                                    color = Color(android.graphics.Color.parseColor("#000000"))
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "${product.nutriments?.fat ?: "Tidak tersedia"} g",
                                    fontFamily = FontFamily(
                                        Font(
                                            resId = R.font.inter_medium,
                                            weight = FontWeight.Medium
                                        )
                                    ),
                                    fontSize = 16.sp,
                                    color = Color(android.graphics.Color.parseColor("#000000"))
                                )
                                Spacer(modifier = Modifier.height(20.dp))

                                //Natrium
                                Text(text = "Natrium",
                                    fontFamily = FontFamily(
                                        Font(
                                            resId = R.font.inter_medium,
                                            weight = FontWeight.Medium
                                        )
                                    ),
                                    fontSize = 16.sp,
                                    color = Color(android.graphics.Color.parseColor("#000000"))
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "${product.nutriments?.salt ?: "Tidak tersedia"} g",
                                    fontFamily = FontFamily(
                                        Font(
                                            resId = R.font.inter_medium,
                                            weight = FontWeight.Medium
                                        )
                                    ),
                                    fontSize = 16.sp,
                                    color = Color(android.graphics.Color.parseColor("#000000"))
                                )


                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.padding(25.dp)) {
                                //Glukosa
                                Text(text = "Gukosa",
                                    fontFamily = FontFamily(
                                        Font(
                                            resId = R.font.inter_medium,
                                            weight = FontWeight.Medium
                                        )
                                    ),
                                    fontSize = 16.sp,
                                    color = Color(android.graphics.Color.parseColor("#000000"))
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "${product.nutriments?.sugars ?: "Tidak tersedia"} g",
                                    fontFamily = FontFamily(
                                        Font(
                                            resId = R.font.inter_medium,
                                            weight = FontWeight.Medium
                                        )
                                    ),
                                    fontSize = 16.sp,
                                    color = Color(android.graphics.Color.parseColor("#000000"))
                                )
                                Spacer(modifier = Modifier.height(20.dp))

                                //Lemak Jenuh
                                Text(text = "Lemak Jenuh",
                                    fontFamily = FontFamily(
                                        Font(
                                            resId = R.font.inter_medium,
                                            weight = FontWeight.Medium
                                        )
                                    ),
                                    fontSize = 16.sp,
                                    color = Color(android.graphics.Color.parseColor("#000000"))
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "${product.nutriments?.saturatedFat ?: "Tidak tersedia"} g",
                                    fontFamily = FontFamily(
                                        Font(
                                            resId = R.font.inter_medium,
                                            weight = FontWeight.Medium
                                        )
                                    ),
                                    fontSize = 16.sp,
                                    color = Color(android.graphics.Color.parseColor("#000000"))
                                )
                                Spacer(modifier = Modifier.height(20.dp))

                                //Kolesterol
                                Text(text = "Kolesterol",
                                    fontFamily = FontFamily(
                                        Font(
                                            resId = R.font.inter_medium,
                                            weight = FontWeight.Medium
                                        )
                                    ),
                                    fontSize = 16.sp,
                                    color = Color(android.graphics.Color.parseColor("#000000"))
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(text = "${product.nutriments?.cholesterol ?: "Tidak tersedia"} g",
                                    fontFamily = FontFamily(
                                        Font(
                                            resId = R.font.inter_medium,
                                            weight = FontWeight.Medium
                                        )
                                    ),
                                    fontSize = 16.sp,
                                    color = Color(android.graphics.Color.parseColor("#000000"))
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                Column (horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()){
                    //Tombol scan ulang
                    Button(onClick = {
                        viewModel.clearFoodInfo()
                        navController.navigate("scanScreen") {
                            popUpTo("scanScreen") { inclusive = true }
                        }
                    }, colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFF00AA16),
                        contentColor = Color.White,
                        disabledBackgroundColor = Color.Gray,
                        disabledContentColor = Color.LightGray
                    )) {
                        Text(text = "Scan Lagi")
                    }
                    Spacer(modifier = Modifier.height(10.dp))

                    Row (horizontalArrangement = Arrangement.Center){
                        //Tombol jangan simpan
                        Button(onClick = {
                            viewModel.clearFoodInfo()

                            navController.navigate("nutrisi") {
                                popUpTo("nutrisi") { inclusive = true }
                            }
                        }, colors = ButtonDefaults.buttonColors(
                                backgroundColor = Color(0xFF00AA16),
                                contentColor = Color.White,
                                disabledBackgroundColor = Color.Gray,
                                disabledContentColor = Color.LightGray
                            )) {
                            Text(text = "Jangan Simpan")
                        }
                        Spacer(modifier = Modifier.width(15.dp))

                        //Tombol simpan
                        Button(onClick = {
                            foodInfo?.product?.let { product ->
                                foodViewModel.addFoodEntry(
                                    imageUrl = product.imageUrl ?: "Tidak tersedia",
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
                            navController.navigate("nutrisi") {
                                popUpTo("nutrisi") { inclusive = true }
                            }
                        }, colors = ButtonDefaults.buttonColors(
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
}

@Preview
@Composable

fun PreviewView(){
    Surface(modifier = Modifier.fillMaxSize()) {
        ResultScreen(navController = rememberNavController(), viewModel = ScanViewModel(), viewModelTwo = FoodViewModel())
    }
}
