package org.wahyuheriyanto.nutricom.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.wahyuheriyanto.nutricom.model.Article

class DataViewModel :ViewModel(){

    private val _weight = MutableStateFlow(0L) // StateFlow untuk nilai points
    private val _height = MutableStateFlow(0L)
    private val _calorie = MutableStateFlow(0L)
    private val _bmi = MutableStateFlow(0L)

    val weight: StateFlow<Long> = _weight
    val height: StateFlow<Long> = _height
    val calorie: StateFlow<Long> = _calorie
    val bmi: StateFlow<Long> = _bmi

    private val _imageUrls = MutableStateFlow<List<String>>(emptyList())
    private val _articles = MutableStateFlow<List<Article>>(emptyList())

    val imageUrls: StateFlow<List<String>> = _imageUrls
    val articles: StateFlow<List<Article>> = _articles

    fun updateData(newWeight: Long,
                     newHeight: Long,
                     newCalorie: Long,
                     newBmi: Long)
    {
        _weight.value = newWeight
        _height.value = newHeight
        _calorie.value = newCalorie
        _bmi.value = newBmi

    }

    fun updateImage(newImage: List<String>)
    {
        _imageUrls.value = newImage

    }

    fun updateArticle(newArticle: List<Article>){
        _articles.value = newArticle
    }

}

expect fun performData(viewModel: AuthViewModel, viewModelTwo: DataViewModel)

expect fun fetchImageUrls(viewModelTwo: DataViewModel)

expect fun fetchLastestArticle(viewModelTwo: DataViewModel)