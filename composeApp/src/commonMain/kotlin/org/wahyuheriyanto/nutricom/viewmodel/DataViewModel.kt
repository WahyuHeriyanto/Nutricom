package org.wahyuheriyanto.nutricom.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.wahyuheriyanto.nutricom.model.Article
import org.wahyuheriyanto.nutricom.model.Nutricions
import org.wahyuheriyanto.nutricom.model.RecommenderItem
import org.wahyuheriyanto.nutricom.model.ScreeningItem

class DataViewModel :ViewModel(){

    private val _weight = MutableStateFlow(0L) // StateFlow untuk nilai points
    private val _height = MutableStateFlow(0L)
    private val _calorie = MutableStateFlow(0L)
    private val _bmi = MutableStateFlow(0L)

    //Nutrisi
    private val _calories = MutableStateFlow(0L)
    private val _sugars = MutableStateFlow(0L)
    private val _fat = MutableStateFlow(0L)
    private val _saturatedFat = MutableStateFlow(0L)
    private val _salt = MutableStateFlow(0L)
    private val _cholesterol = MutableStateFlow(0L)

    val weight_value: StateFlow<Long> = _weight
    val height_value: StateFlow<Long> = _height
    val calorie: StateFlow<Long> = _calorie
    val bmi: StateFlow<Long> = _bmi

    //nutrisi
    val calories: StateFlow<Long> = _calories
    val sugars: StateFlow<Long> = _sugars
    val fat: StateFlow<Long> = _fat
    val saturatedFat: StateFlow<Long> = _saturatedFat
    val salt: StateFlow<Long> = _salt
    val cholesterol: StateFlow<Long> = _cholesterol


    private val _imageUrls = MutableStateFlow<List<String>>(emptyList())
    private val _articles = MutableStateFlow<List<Article>>(emptyList())
    private val _recommenders = MutableStateFlow<List<RecommenderItem>>(emptyList())
    private val _screeningResults = MutableStateFlow<List<ScreeningItem>>(emptyList())
    private val _nutricions = MutableStateFlow<List<Nutricions>>(emptyList())


    val imageUrls: StateFlow<List<String>> = _imageUrls
    val articles: StateFlow<List<Article>> = _articles
    val recommenders: StateFlow<List<RecommenderItem>> = _recommenders
    val screeningResults : StateFlow<List<ScreeningItem>> = _screeningResults
    val nutri : StateFlow<List<Nutricions>> = _nutricions

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

    fun updateRecommender(newRec : List<RecommenderItem>){
        _recommenders.value = newRec
    }

    fun updateScreening(newScreen: List<ScreeningItem>){
        _screeningResults.value = newScreen
    }

    fun updateNutricion(newCalories:Long, newSugar:Long, newFat:Long, newSaturatedFat:Long,
                        newSalt:Long, newCholesterol:Long){
        _calories.value = newCalories
        _sugars.value = newSugar
        _fat.value = newFat
        _saturatedFat.value = newSaturatedFat
        _salt.value = newSalt
        _cholesterol.value = newCholesterol
    }

}

expect fun performData(viewModel: AuthViewModel, viewModelTwo: DataViewModel)

expect fun performDataLogin(viewModel: AuthViewModel, viewModelTwo: DataViewModel, uid:String?)

expect fun fetchImageUrls(viewModelTwo: DataViewModel)

expect fun fetchLastestArticle(viewModelTwo: DataViewModel)

expect fun fetchAllArticle(viewModelTwo: DataViewModel)

expect fun fetchRecommender(viewModelTwo: DataViewModel)

expect fun fetchScreningResult(viewModelTwo: DataViewModel)

expect fun fetchNutricions(viewModelTwo: DataViewModel)