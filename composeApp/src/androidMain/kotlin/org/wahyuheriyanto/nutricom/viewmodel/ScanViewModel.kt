package org.wahyuheriyanto.nutricom.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.wahyuheriyanto.nutricom.data.FoodInfoResponse
import org.wahyuheriyanto.nutricom.network.retrofit.RetrofitInstance

class ScanViewModel : ViewModel() {
    private val _foodInfo = MutableStateFlow<FoodInfoResponse?>(null)
    val foodInfo: StateFlow<FoodInfoResponse?> = _foodInfo

    fun fetchFoodInfo(barcode: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getFoodInfo(barcode)
                if (response.isSuccessful) {
                    _foodInfo.value = response.body()
                } else {
                    _foodInfo.value = null
                }
            } catch (e: Exception) {
                Log.e("ScanViewModel", "Error fetching food info", e)
                _foodInfo.value = null
            }
        }
    }
}

