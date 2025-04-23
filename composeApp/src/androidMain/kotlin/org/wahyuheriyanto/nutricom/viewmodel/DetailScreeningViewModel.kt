package org.wahyuheriyanto.nutricom.viewmodel

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

sealed class ScreeningDetailState {
    object Loading : ScreeningDetailState()
    data class Umum(val data: UmumData) : ScreeningDetailState()
    data class Diabetes(val data: DiabetesData) : ScreeningDetailState()
    data class Cardio(val data: CardioData) : ScreeningDetailState()
    data class Error(val message: String) : ScreeningDetailState()
}

data class UmumData(
    val type: String = "",
    val height: Double = 0.0,
    val weight: Double = 0.0,
    val bmi: Double = 0.0,
    val apHi: Double = 0.0,
    val apLo: Double = 0.0,
    val cardio: Double = 0.0,
    val diabetes: Double = 0.0,
    val cholesterol: Double = 0.0,
    val gluc: Double = 0.0,
    val smoke: Double = 0.0,
    val alco: Double = 0.0,
    val active: Double = 0.0,
    val sleep: Double = 0.0,
    val ldl: Double = 0.0,
    val hdl: Double = 0.0,
    val tri: Double = 0.0,
    val hba1c: Double = 0.0,
    val healthComplaint: String? = null,
    val timestamp: Long = 0L
)

data class DiabetesData(
    val type: String = "",
    val gender: Double = 0.0,
    val age: Double = 0.0,
    val hypertension: Double = 0.0,
    val heartDisease: Double = 0.0,
    val smokingHistory: Double = 0.0,
    val bmi: Double = 0.0,
    val hbA1c: Double = 0.0,
    val bloodGlucose: Double = 0.0,
    val prediction: String = "",
    val timestamp: Long = 0L
)

data class CardioData(
    val type: String = "",
    val age: Double = 0.0,
    val gender: Double = 0.0,
    val height: Double = 0.0,
    val weight: Double = 0.0,
    val apHi: Double = 0.0,
    val apLo: Double = 0.0,
    val cholesterol: Double = 0.0,
    val gluc: Double = 0.0,
    val smoke: Double = 0.0,
    val alco: Double = 0.0,
    val active: Double = 0.0,
    val timestamp: Long = 0L
)

class DetailScreeningViewModel : ViewModel() {
    var state by mutableStateOf<ScreeningDetailState>(ScreeningDetailState.Loading)
        private set

    fun loadDetail(type: String, documentId: String) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val db = FirebaseFirestore.getInstance()

        db.collection("screening")
            .document(userId)
            .collection("active")
            .document(documentId)
            .get()
            .addOnSuccessListener { result ->
                if (result.exists()) {
                    when (type) {
                        "Umum" -> result.toObject(UmumData::class.java)?.let {
                            state = ScreeningDetailState.Umum(it)
                        } ?: run {
                            state = ScreeningDetailState.Error("Failed to parse Umum data")
                        }

                        "Diabetes" -> result.toObject(DiabetesData::class.java)?.let {
                            state = ScreeningDetailState.Diabetes(it)
                        } ?: run {
                            state = ScreeningDetailState.Error("Failed to parse Diabetes data")
                        }

                        "Kardiovaskular" -> result.toObject(CardioData::class.java)?.let {
                            state = ScreeningDetailState.Cardio(it)
                        } ?: run {
                            state = ScreeningDetailState.Error("Failed to parse Cardio data")
                        }

                        else -> state = ScreeningDetailState.Error("Unknown type: $type")
                    }
                } else {
                    state = ScreeningDetailState.Error("No document found with this ID")
                }
            }
            .addOnFailureListener { e ->
                state = ScreeningDetailState.Error(e.message ?: "Unknown error")
            }
    }
}