package org.wahyuheriyanto.nutricom.view.screens

import android.util.Log
import android.widget.Toast
import coil.compose.AsyncImage
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.viewmodel.DataPredictViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DiabetesViewModel

@Composable
fun ResultPredictScreen(navController: NavController, viewModel: DiabetesViewModel, dataPredictViewModel : DataPredictViewModel){
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val prediction by viewModel.prediction.collectAsState(initial = -1)
//    Log.e("cekForce","lewat hasil udah mau ditampilin")
//
    Log.e("cekprediksi","satu $prediction")

    val imageRes = when (prediction) {
        1 -> R.drawable.berpotensi_diabetes
        0 -> R.drawable.tidak_berpotensi_diabetes
        else -> R.drawable.green_box_signin // default jika belum ada hasil
    }

    val status = if (prediction == 1) "beresiko Diabetes" else "tidak beresiko Diabetes"

    val rekomendasiList = if (prediction == 1) listOf(
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/dokter.png?alt=media&token=b6334aa9-e0db-4ea8-9b54-d7d73eaf531c",
            sentence = "Pergi ke fasilitas kesehatan terdekat dan berkonsultasi dengan dokter"
        ),
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/sugar.png?alt=media&token=bbef9ce0-cca3-4572-ae2e-536246dafaed",
            sentence = "Hindari konsumsi makanan tinggi gula"
        ),
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/workout.png?alt=media&token=acc495fd-1734-40c0-95fb-f57f8b398586",
            sentence = "Meningkatkan aktivitas fisik"
        ),
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/sleep.png?alt=media&token=bf2ef34e-e44d-4637-8264-b7fe4bf17876",
            sentence = "Waktu tidur yang berkualitas"
        ),
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/smoking.png?alt=media&token=735efd93-ffa5-48d6-b1d8-2817abd96af9",
            sentence = "Hindari rokok dan alkohol"
        ),
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/luka.png?alt=media&token=539f6527-ad8a-4317-8572-557511b08371",
            sentence = "Hindari luka fisik"
        )
    ) else listOf(
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/food.png?alt=media&token=02efb5dc-3b6d-4c4c-a48c-5df61320f6ab",
            sentence = "Jaga pola makan yang sehat dan bergizi"
        ),
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/sugar.png?alt=media&token=bbef9ce0-cca3-4572-ae2e-536246dafaed",
            sentence = "Mengurangi konsumsi makanan dengan gula berlebih"
        ),
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/workout.png?alt=media&token=acc495fd-1734-40c0-95fb-f57f8b398586",
            sentence = "Rutin berolahraga"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
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
            .height(520.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(color = Color(android.graphics.Color.parseColor("#D2D2D2"))),
        ){
            Column (modifier = Modifier.padding(30.dp)){
                Image(painter = painterResource(id = imageRes),
                    contentDescription = "",
                    modifier = Modifier
                        .width(300.dp)
                        .height(180.dp))
                Spacer(modifier = Modifier.height(15.dp))
                Text(
                    text = "Berdasarkan pemeriksaan, anda $status. Berikut hal yang disarankan untuk anda",
                    fontFamily = FontFamily(
                        Font(
                            resId = R.font.inter_semibold,
                            weight = FontWeight.Bold
                        )
                    ),
                    fontSize = 14.sp,
                    color = Color.Black,
                    textAlign = TextAlign.Justify
                )
                Spacer(modifier = Modifier.height(8.dp))
                LazyRow(
                    modifier = Modifier
                        .width(340.dp)
                        .height(120.dp)
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(rekomendasiList) { item ->
                        AsyncImage(
                            model = item.imageUrl,
                            contentDescription = item.sentence,
                            modifier = Modifier
                                .size(100.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.LightGray),
                            contentScale = ContentScale.Crop
                        )
                    }

                }


                Row(verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center){
                    Button(
                        onClick = {
                            saveRecommendationsIfPredictionIsPositive(rekomendasiList)
                            dataPredictViewModel.sendDataDiabetesToFirestore(prediction, onSuccess = {
                                Toast.makeText(context, "Berhasil disimpan!", Toast.LENGTH_SHORT).show()
                                navController.navigate("skrining")
                            },
                                onError = {
                                    Toast.makeText(context, "Gagal: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                                })
                        },
                        modifier = Modifier.padding(top = 16.dp)
                            .background(Color.Transparent),
                        colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#00AA16")))
                    ) {
                        Text("Simpan")
                    }
                    Spacer(modifier = Modifier.width(20.dp))

                    Button(onClick = {
                        navController.navigate("skrining")
                    }, modifier = Modifier.padding(top = 16.dp)
                        .background(Color.Transparent),
                        colors = ButtonDefaults.buttonColors(Color(android.graphics.Color.parseColor("#00AA16")))
                    ) {
                        Text(text = "Jangan Simpan")
                    }
                }
            }
        }
    }
}