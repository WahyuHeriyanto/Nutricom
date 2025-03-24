package org.wahyuheriyanto.nutricom.view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import org.wahyuheriyanto.nutricom.model.Article
import org.wahyuheriyanto.nutricom.view.components.ArticleItem
import org.wahyuheriyanto.nutricom.view.components.ArticleLoading
import org.wahyuheriyanto.nutricom.viewmodel.AuthViewModel
import org.wahyuheriyanto.nutricom.viewmodel.DataViewModel
import org.wahyuheriyanto.nutricom.viewmodel.LoginState
import org.wahyuheriyanto.nutricom.viewmodel.fetchAllArticle
@Composable
fun ArticleScreen(viewModel: AuthViewModel, viewModelTwo : DataViewModel) {
    val loginState: LoginState by viewModel.loginState.collectAsState()
    val scrollState = rememberScrollState()
    val articleContent by viewModelTwo.articles.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    var filteredArticles by remember { mutableStateOf(articleContent) }

    fetchAllArticle(viewModelTwo = viewModelTwo)

    LaunchedEffect(Unit) {
        fetchAllArticle(viewModelTwo)
    }

    LaunchedEffect(articleContent) {
        filteredArticles = articleContent // Pastikan data awal tampil
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(Color(android.graphics.Color.parseColor("#F4F4F4")))
    ) {
        ConstraintLayout {

            val startGuideline = createGuidelineFromStart(0.1f)
            val endGuideline = createGuidelineFromEnd(0.1f)
            val (search, articles) = createRefs()

            // Search Bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .background(Color.White, shape = RoundedCornerShape(8.dp))
                    .constrainAs(search) {
                        top.linkTo(parent.top)
                        start.linkTo(startGuideline)
                        end.linkTo(endGuideline)
                    }
                    .padding(horizontal = 12.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = searchQuery,
                    onValueChange = { query ->
                        searchQuery = query
                        filteredArticles = if (query.isEmpty()) {
                            articleContent
                        } else {
                            articleContent.filter { article ->
                                query.split(" ").any { word ->
                                    article.title.contains(word, ignoreCase = true)
                                }
                            }
                        }
                    },
                    placeholder = { Text("Cari artikel...") },
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    )
                )
                if (searchQuery.isNotEmpty()) {
                    IconButton(onClick = {
                        searchQuery = ""
                        filteredArticles = articleContent
                    }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Clear")
                    }
                }
            }

            Column(
                modifier = Modifier
                    .width(320.dp)
                    .height(520.dp)
                    .background(color = Color.Transparent)
                    .constrainAs(articles) {
                        top.linkTo(search.bottom, margin = 10.dp)
                        start.linkTo(startGuideline)
                        end.linkTo(endGuideline)
                    }
            ) {
                if (filteredArticles.isEmpty() && searchQuery.isNotEmpty()) {
                    Text(
                        text = "Artikel tidak ditemukan",
                        fontSize = 16.sp,
                        color = Color.Red,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                } else {
                    when (loginState) {
                        is LoginState.Loading -> {
                            repeat(articleContent.size) {
                                ArticleLoading()
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        is LoginState.Success -> {
                            filteredArticles.forEach { article ->
                                ArticleItem(article = article)
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                        else -> {
                            repeat(articleContent.size) {
                                ArticleLoading()
                                Spacer(modifier = Modifier.height(8.dp))
                            }
                        }
                    }
                }
            }

        }


        }

}