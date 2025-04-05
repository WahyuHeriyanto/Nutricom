package org.wahyuheriyanto.nutricom.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.view.factory.DiabetesViewModelFactory
import org.wahyuheriyanto.nutricom.viewmodel.DiabetesViewModel

@Composable
fun ResultPredictScreen(navController: NavController, viewModel: DiabetesViewModel){
    val scrollState = rememberScrollState()
    val horizonScrollState = rememberScrollState()
    val context = LocalContext.current
    val prediction by viewModel.prediction.collectAsState()

    Log.e("cekprediksi","satu $prediction")

    val imageRes = when (prediction) {
        1 -> R.drawable.gula
        0 -> R.drawable.green_box_signin
        else -> R.drawable.scan // default jika belum ada hasil
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp)
    ) {
        Text(
            text = "Hasil Screening",
            fontFamily = FontFamily(
                Font(
                    resId = R.font.inter_semibold,
                    weight = FontWeight.Bold
                )
            ),
            fontSize = 22.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(20.dp))
        Box(modifier = Modifier
            .width(340.dp)
            .height(450.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(color = Color(android.graphics.Color.parseColor("#D2D2D2"))),
        ){
            Column (modifier = Modifier.padding(30.dp)){
                Image(painter = painterResource(id = imageRes),
                    contentDescription = "",
                    modifier = Modifier
                        .width(300.dp)
                        .height(180.dp))
                Spacer(modifier = Modifier.height(20.dp))
                Row (modifier = Modifier
                    .width(340.dp)
                    .height(120.dp)
                    .horizontalScroll(horizonScrollState)
                    .padding(20.dp)
                    .background(color = Color.White),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center){
                    Box(modifier = Modifier
                        .size(60.dp)
                        .background(color = Color(android.graphics.Color.parseColor("#D2D2D2"))))
                    Spacer(modifier = Modifier.width(15.dp))
                    Box(modifier = Modifier
                        .size(60.dp)
                        .background(color = Color(android.graphics.Color.parseColor("#D2D2D2"))))
                    Spacer(modifier = Modifier.width(15.dp))
                    Box(modifier = Modifier
                        .size(60.dp)
                        .background(color = Color(android.graphics.Color.parseColor("#D2D2D2"))))
                    Spacer(modifier = Modifier.width(15.dp))
                    Box(modifier = Modifier
                        .size(60.dp)
                        .background(color = Color(android.graphics.Color.parseColor("#D2D2D2"))))
                    Spacer(modifier = Modifier.width(15.dp))
                    Box(modifier = Modifier
                        .size(60.dp)
                        .background(color = Color(android.graphics.Color.parseColor("#D2D2D2"))))
                    Spacer(modifier = Modifier.width(15.dp))
                    Box(modifier = Modifier
                        .size(60.dp)
                        .background(color = Color(android.graphics.Color.parseColor("#D2D2D2"))))
                }

                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center){
                    Button(
                        onClick = {

                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Simpan")
                    }
                    Spacer(modifier = Modifier.width(20.dp))

                    Button(onClick = {
                        navController.navigate("activity")
                    }, modifier = Modifier.padding(top = 16.dp)) {
                        Text(text = "Jangan Simpan")
                    }
                }
            }
        }
    }
}