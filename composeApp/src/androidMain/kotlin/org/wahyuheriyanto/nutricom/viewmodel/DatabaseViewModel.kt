package org.wahyuheriyanto.nutricom.viewmodel

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


fun DatabaseViewModel(viewModel: AuthViewModel,userId: String) {
    CoroutineScope(Dispatchers.IO).launch {
        val firestore = FirebaseFirestore.getInstance()
        try {
            val document = firestore.collection("users").document(userId).get().await()
            val pointsValue = document.getLong("point") ?: 0L

            viewModel.updatePoints(pointsValue) // Perbarui points di ViewModel
            Log.e("CekPoint", "Point : $pointsValue")

        } catch (e: Exception) {
            Log.e("FirestoreError", "Error getting points", e)
        }
    }
}