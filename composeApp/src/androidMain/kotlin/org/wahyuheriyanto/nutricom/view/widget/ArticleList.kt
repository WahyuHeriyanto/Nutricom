package org.wahyuheriyanto.nutricom.view.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import org.wahyuheriyanto.nutricom.model.Article
import org.wahyuheriyanto.nutricom.viewmodel.DataViewModel


@Composable
fun ArticleLoading(){
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
fun ArticleItem(article: Article) {

        Box(modifier = Modifier.clip(RoundedCornerShape(8.dp))
            .background(color = Color.White)
            )
        {
            Row(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .background(color = Color.White)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(article.imageUrl),
                    contentDescription = "Article Image",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.Transparent),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = article.title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = article.content.take(50) + "...", // Potong isi artikel
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }
            }

        }


}