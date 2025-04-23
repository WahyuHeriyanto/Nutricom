package org.wahyuheriyanto.nutricom.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.data.model.ScreeningItem


@Composable
fun ScreeningLoading(){
    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp))
        .height(96.dp)
        .background(color = Color.LightGray)
        .shimmerLoadingAnimation()
    ){
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .background(color = Color.LightGray)
                .shimmerLoadingAnimation()
        ){

        }
    }
}

@Composable
fun ScreeningItem(screening: ScreeningItem, navController: NavController) {

    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp))
        .background(color = Color.White)
        .clickable {
            navController.navigate("detailScreeningScreen/${screening.type}/${screening.id}")
        }
    )
    {
        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .background(color = Color.White),
            horizontalArrangement = Arrangement.Center
        ) {
            Image(
                painter = rememberAsyncImagePainter(screening.imageUrl),
                contentDescription = "Article Image",
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = Color.Transparent),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = screening.timestamp.toString(),
                    fontFamily = FontFamily(
                        Font(
                            resId = R.font.inter_medium,
                            weight = FontWeight.Medium
                        )
                    ),
                    fontSize = 16.sp,
                    color = Color(android.graphics.Color.parseColor("#737373"))
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = screening.type,
                    fontFamily = FontFamily(
                        Font(
                            resId = R.font.inter_medium,
                            weight = FontWeight.Medium
                        )
                    ),
                    fontSize = 20.sp,
                    color = Color(android.graphics.Color.parseColor("#00AA16"))
                )
            }
        }

    }


}