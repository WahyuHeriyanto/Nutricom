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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import org.wahyuheriyanto.nutricom.data.model.ConsumtionItem
import org.wahyuheriyanto.nutricom.viewmodel.ScanViewModel


@Composable
fun ConsumtionLoading(){
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
fun ConsumtionItem(consumtion: ConsumtionItem, navController: NavController, viewModel: ScanViewModel, onDeleteClick: () -> Unit) {
    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp))
        .background(color = Color.White)
        .clickable {
            val id = consumtion.id
            val calories = consumtion.calories.toString()
            val fat = consumtion.fat.toString()
            val saturatedFat = consumtion.saturatedFat.toString()
            val cholesterol = consumtion.cholesterol.toString()
            val sugars = consumtion.sugars.toString()
            val salt = consumtion.salt.toString()
            navController.navigate("detailFoodScreen/${id}/${consumtion.imageUrl}/${consumtion.name}/${calories}/${fat}/${saturatedFat}/${cholesterol}/${sugars}/${salt}")
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
                painter = rememberAsyncImagePainter(consumtion.imageUrl),
                contentDescription = "Article Image",
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(
                modifier = Modifier
                    .background(color = Color.Transparent),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = consumtion.name,
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
                    text = consumtion.calories.toString(),
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
            IconButton(onClick = onDeleteClick) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }

    }


}