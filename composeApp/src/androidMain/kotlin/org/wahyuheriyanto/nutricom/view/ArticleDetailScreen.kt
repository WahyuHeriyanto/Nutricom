package org.wahyuheriyanto.nutricom.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import org.wahyuheriyanto.nutricom.R

@Composable
fun ArticleDetailScreen(title: String, author: String, content: String, imageUrl: String){

    val scrollState = rememberScrollState()


    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(scrollState)) {

        Text(
            text = title,
            fontFamily = FontFamily(
                Font(
                    resId = R.font.inter_semibold,
                    weight = FontWeight.Bold
                )
            ),
            fontSize = 22.sp,
            color = Color.Black
        )
        Spacer(modifier = Modifier.height(15.dp))
        Image(painter = rememberAsyncImagePainter(imageUrl), contentDescription ="",
            modifier = Modifier
                .width(320.dp)
                .height(180.dp)
                .clip(RoundedCornerShape(20.dp)),
            alignment = Alignment.Center
        )
        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = "Penulis : $author",
            fontFamily = FontFamily(
                Font(
                    resId = R.font.inter_medium,
                    weight = FontWeight.Medium
                )
            ),
            fontSize = 20.sp,
            color = androidx.compose.ui.graphics.Color.Black
        )

        Spacer(modifier = Modifier.height(15.dp))
        Text(
            text = content,
            fontFamily = FontFamily(
                Font(
                    resId = R.font.inter_medium,
                    weight = FontWeight.Medium
                )
            ),
            fontSize = 20.sp,
            color = androidx.compose.ui.graphics.Color.Black,
            textAlign = TextAlign.Justify
        )
    }
}