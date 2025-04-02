package org.wahyuheriyanto.nutricom.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.wahyuheriyanto.nutricom.data.FoodInfoResponse
import org.wahyuheriyanto.nutricom.network.retrofit.RetrofitInstance
import java.util.Date

class ScanViewModel : ViewModel() {
    private val _foodInfo = MutableStateFlow<FoodInfoResponse?>(null)
    val foodInfo: StateFlow<FoodInfoResponse?> = _foodInfo

    fun fetchFoodInfo(barcode: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getFoodInfo(barcode)
                if (response.isSuccessful) {
                    _foodInfo.value = response.body()
                    Log.e("cekbody","${response.body()}")
                } else {
                    _foodInfo.value = null
                }
            } catch (e: Exception) {
                Log.e("ScanViewModel", "Error fetching food info", e)
                _foodInfo.value = null
            }
        }
    }

    fun clearFoodInfo(){
        viewModelScope.launch {
            try {
                _foodInfo.value = null
            } catch (e: Exception) {
                Log.e("ScanViewModel", "Error fetching food info", e)
                _foodInfo.value = null
            }
        }
    }
}

