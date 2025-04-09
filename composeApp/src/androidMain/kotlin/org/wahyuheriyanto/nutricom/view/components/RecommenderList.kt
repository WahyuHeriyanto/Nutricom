package org.wahyuheriyanto.nutricom.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.Checkbox
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import org.wahyuheriyanto.nutricom.R
import org.wahyuheriyanto.nutricom.model.RecommenderItem
import org.wahyuheriyanto.nutricom.model.ScreeningItem

@Composable

fun RecListActive(recItem: RecommenderItem){
    var checked by remember { mutableStateOf(true) }

    Row (modifier = Modifier
        .width(270.dp)
        .background(color = Color.Transparent)
        .drawBehind {
            val borderSize = 2.dp.toPx()
            drawLine(
                color = Color(android.graphics.Color.parseColor("#D9D9D9")),
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = borderSize
            )

        },
        horizontalArrangement = Arrangement.SpaceBetween, // Elemen kiri dan kanan berjauhan
        verticalAlignment = Alignment.CenterVertically)
    {
        Text(
            text = recItem.sentence,
            fontFamily = FontFamily(
                Font(
                    resId = R.font.inter_medium,
                    weight = FontWeight.Medium
                )
            ),
            fontSize = 12.sp,
            color = Color.Black,
            modifier = Modifier.width(220.dp)
        )

        Checkbox(
            checked = checked,
            onCheckedChange = { checked = it }
        )


    }
}

@Composable
fun RecommenderActive(recItem: RecommenderItem) {

    Box(modifier = Modifier.clip(RoundedCornerShape(8.dp))
        .background(color = Color.White)
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
                painter = rememberAsyncImagePainter(recItem.imageUrl),
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
                androidx.compose.material3.Text(
                    text = recItem.sentence,
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
                androidx.compose.material3.Text(
                    text = recItem.sentence,
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