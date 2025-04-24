package org.wahyuheriyanto.nutricom.view.screens


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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.viewmodel.CardioViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DataPredictViewModel

@Composable
fun ResultPredictScreenCardio(navController: NavController, viewModel: CardioViewModel, dataPredictViewModel : DataPredictViewModel){
    val scrollState = rememberScrollState()
    val context = LocalContext.current
    val prediction by viewModel.prediction.collectAsState()

    val imageRes = when (prediction) {
        1 -> R.drawable.berpotensi_kardiovaskular
        0 -> R.drawable.tidak_berpotensi_kardiovaskular
        else -> R.drawable.green_box_signin // default jika belum ada hasil
    }

    val rekomendasiList = if (prediction == 1) listOf(
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/page1.jpg?alt=media&token=751d6001-a63c-4a92-969f-8cc912e0cfaa",
            sentence = "Pergi ke fasilitas kesehatan terdekat dan berkonsultasi dengan dokter"
        ),
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/page1.jpg?alt=media&token=751d6001-a63c-4a92-969f-8cc912e0cfaa",
            sentence = "Mengurangi konsumsi makanan tinggi gula"
        ),
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/page1.jpg?alt=media&token=751d6001-a63c-4a92-969f-8cc912e0cfaa",
            sentence = "Meningkatkan aktivitas fisik"
        ),
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/page1.jpg?alt=media&token=751d6001-a63c-4a92-969f-8cc912e0cfaa",
            sentence = "Waktu tidur yang berkualitas"
        ),
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/page1.jpg?alt=media&token=751d6001-a63c-4a92-969f-8cc912e0cfaa",
            sentence = "Hindari rokok dan alkohol"
        ),
        RecommendationData(
            imageUrl = "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/page1.jpg?alt=media&token=751d6001-a63c-4a92-969f-8cc912e0cfaa",
            sentence = "Hindari luka fisik"
        )
    ) else emptyList()

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
                            dataPredictViewModel.sendDataCardioToFirestore(prediction, onSuccess = {
                                Toast.makeText(context, "Berhasil disimpan!", Toast.LENGTH_SHORT).show()
                                navController.navigate("skrining")
                            },
                                onError = {
                                    Toast.makeText(context, "Gagal: ${it.localizedMessage}", Toast.LENGTH_SHORT).show()
                                })

                        },
                        modifier = Modifier.padding(top = 16.dp)
                    ) {
                        Text("Simpan")
                    }
                    Spacer(modifier = Modifier.width(20.dp))

                    Button(onClick = {
                        navController.navigate("skrining")
                    }, modifier = Modifier.padding(top = 16.dp)) {
                        Text(text = "Jangan Simpan")
                    }
                }
            }
        }
    }
}