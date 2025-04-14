package org.wahyuheriyanto.nutricom.view.components

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import org.wahyuheriyanto.nutricom.data.model.Article


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
fun ArticleItem(article: Article, navController: NavController) {

        Box(modifier = Modifier.clip(RoundedCornerShape(8.dp))
            .background(color = Color.White)
            .clickable {
                val encodedTitle = Uri.encode(article.title)
                val encodedAuthor = Uri.encode(article.author)
                val encodedContent = Uri.encode(article.content)
                val encodedImageUrl = Uri.encode(article.imageUrl)

                navController.navigate(
                    "articleDetailScreen/$encodedTitle/$encodedAuthor/$encodedContent/$encodedImageUrl"
                )
            }
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