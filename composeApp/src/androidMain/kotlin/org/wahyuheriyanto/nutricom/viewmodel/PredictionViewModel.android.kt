package org.wahyuheriyanto.nutricom.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.wahyuheriyanto.nutricom.data.InputData
import org.wahyuheriyanto.nutricom.data.repository.PredictionRepository

// PredictionViewModel.kt
class PredictionViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()
    private val repository = PredictionRepository()

    private val _predictionResult = MutableStateFlow<List<Float>?>(null)
    val predictionResult: StateFlow<List<Float>?> = _predictionResult

    fun getPrediction(inputData: InputData) {
        viewModelScope.launch {
            try {
                val response = repository.fetchPrediction(inputData)
                _predictionResult.value = response.prediction
            } catch (e: Exception) {
                _predictionResult.value = null
                e.printStackTrace()
            }
        }
    }

    fun savePredictData(barcode: String, name: String, calories: Double, sugars:Double, cholesterol: Double, fat: Double, salt: Double, saturatedFat: Double){
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Buat objek data makanan
        val foodEntry = FoodEntry(barcode, name, calories, sugars, cholesterol, fat, salt, saturatedFat)

        // Simpan ke Firestore di path consume/{uid}/food/{auto_id}
        db.collection("consume").document(userId)
            .collection("food")
            .add(foodEntry)  // Firestore akan otomatis buat Auto ID
            .addOnSuccessListener { documentReference ->
                println("Data berhasil disimpan dengan ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Gagal menyimpan data: ${e.localizedMessage}")
            }


    }
}


