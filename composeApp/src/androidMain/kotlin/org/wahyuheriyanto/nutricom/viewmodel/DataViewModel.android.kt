package org.wahyuheriyanto.nutricom.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

actual fun performData(viewModel: AuthViewModel, viewModelTwo: DataViewModel){
    CoroutineScope(Dispatchers.IO).launch {
        try {

            val currentuid = viewModel.uid.value
            Log.e("CekPointWeight", "Point : $currentuid")
            currentuid?.let { currentUser ->
                val uids = currentUser
                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("datas").document(uids).get()
                    .addOnSuccessListener { document ->
                        Log.e("CekPointWeight", "Point : $uids")
                        val weightValue = document.getLong("weight") ?: 0L
                        val heightValue = document.getLong("height") ?: 0L
                        val calorieValue = document.getLong("calorie") ?: 0L
                        val bmiValue = document.getLong("bmi") ?: 0L
                        when {
                            weightValue != 0L -> {
                                viewModelTwo.updateData(weightValue,heightValue,calorieValue,bmiValue)
                                Log.e("CekPointWeight", "Point : $weightValue")
                            }
                            else -> {
                                Log.e("Errorkukuku","Belum keisi")
                            }
                        }
                    }


            }


        }catch(e : Exception){
            Log.e("uid","belum dapet uid")

    }
    }

}