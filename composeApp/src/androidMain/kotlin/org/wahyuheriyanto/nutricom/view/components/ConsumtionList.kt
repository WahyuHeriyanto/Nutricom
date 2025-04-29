package org.wahyuheriyanto.nutricom.view.components

import android.util.Log
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.data.model.ConsumtionItem
import org.wahyuheriyanto.nutricom.viewmodel.ScanViewModel


@Composable
fun ConsumtionLoading(){
    Box(modifier = Modifier
        .clip(RoundedCornerShape(8.dp))
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
    Box(modifier = Modifier
        .clip(RoundedCornerShape(8.dp))
        .background(color = Color.White)
        .clickable {
            val id = consumtion.id
            val calories = consumtion.calories.toString()
            val fat = consumtion.fat.toString()
            val saturatedFat = consumtion.saturatedFat.toString()
            val cholesterol = consumtion.cholesterol.toString()
            val sugars = consumtion.sugars.toString()
            val salt = consumtion.salt.toString()
            navController.navigate("detailFoodScreen/${consumtion.name}/${calories}/${fat}/${saturatedFat}/${cholesterol}/${sugars}/${salt}")
        }
        .padding(8.dp)
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Gambar makanan
            Image(
                painter = rememberAsyncImagePainter(consumtion.imageUrl),
                contentDescription = "Food Image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.width(12.dp))

            // Nama makanan + Kalori
            Column(
                modifier = Modifier
                    .weight(1f) // Biar kolom ini fleksibel lebarnya
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
                    color = Color(0xFF212121),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${consumtion.calories} Kalori",
                    fontFamily = FontFamily(
                        Font(
                            resId = R.font.inter_medium,
                            weight = FontWeight.Normal
                        )
                    ),
                    fontSize = 14.sp,
                    color = Color(0xFF737373)
                )
            }

            // Tombol hapus
            IconButton(
                onClick = onDeleteClick
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.Red
                )
            }
        }
    }

}