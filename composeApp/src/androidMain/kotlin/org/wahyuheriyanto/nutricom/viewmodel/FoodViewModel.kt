package org.wahyuheriyanto.nutricom.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ServerTimestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.util.Date

data class FoodEntry(
    val imageUrl: String,
    val barcode: String,
    val name: String,
    val calories: Double,
    val sugars: Double,
    val cholesterol: Double,
    val fat: Double,
    val salt: Double,
    val saturatedFat: Double,
    @ServerTimestamp val timestamp: Date? = null
)

class FoodViewModel : ViewModel() {
    private val db = FirebaseFirestore.getInstance()

    fun addFoodEntry(imageUrl:String, barcode: String, name: String, calories: Double, sugars:Double, cholesterol: Double, fat: Double, salt: Double, saturatedFat: Double) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        // Buat objek data makanan
        val foodEntry = FoodEntry(imageUrl, barcode, name, calories, sugars, cholesterol, fat, salt, saturatedFat)

        // Simpan ke Firestore di path consume/{uid}/food/{auto_id}
        db.collection("consume").document(userId)
            .collection("food")
            .add(foodEntry)  // Firestore akan otomatis buat Auto ID
            .addOnSuccessListener { documentReference ->
                calculateNutricion()
                println("Data berhasil disimpan dengan ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                println("Gagal menyimpan data: ${e.localizedMessage}")
            }
    }

    fun calculateNutricion (){
            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

            val foodCollection = db.collection("consume").document(userId).collection("food")
            val nutricionDoc = db.collection("nutricions").document(userId)

            foodCollection.get()
                .addOnSuccessListener { documents ->
                    var totalCalories = 0.0
                    var totalSugars = 0.0
                    var totalFat = 0.0
                    var totalSalt = 0.0
                    var totalSaturatedFat = 0.0
                    var totalCholesterol = 0.0

                    for (document in documents) {
                        totalCalories += document.getDouble("calories") ?: 0.0
                        totalSugars += document.getDouble("sugars") ?: 0.0
                        totalFat += document.getDouble("fat") ?: 0.0
                        totalSalt += document.getDouble("salt") ?: 0.0
                        totalSaturatedFat += document.getDouble("saturatedFat") ?: 0.0
                        totalCholesterol += document.getDouble("cholesterol") ?: 0.0
                    }

                    val nutricionData = mapOf(
                        "kalori" to totalCalories,
                        "glukosa" to totalSugars,
                        "lemak" to totalFat,
                        "natrium" to totalSalt,
                        "lemakJenuh" to totalSaturatedFat,
                        "kolesterol" to totalCholesterol
                    )

                    nutricionDoc.set(nutricionData)
                        .addOnSuccessListener {
                            println("Data nutrisi berhasil diperbarui")
                        }
                        .addOnFailureListener { e ->
                            println("Gagal memperbarui data nutrisi: ${e.localizedMessage}")
                        }
                }
                .addOnFailureListener { e ->
                    println("Gagal mengambil data makanan: ${e.localizedMessage}")
                }
    }

    private val _maxDailyIntake = MutableStateFlow<Map<String, Double>>(emptyMap())
    val maxDailyIntake: StateFlow<Map<String, Double>> = _maxDailyIntake.asStateFlow()

    fun calculateMaxDailyIntake() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val userDoc = db.collection("datas").document(userId)

        userDoc.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val age = document.getDouble("age") ?: return@addOnSuccessListener
                    val weight = document.getDouble("weight") ?: return@addOnSuccessListener
                    val height = document.getDouble("height") ?: return@addOnSuccessListener
                    val bmi = document.getDouble("bmi") ?: return@addOnSuccessListener
                    val activity = document.getDouble("activity") ?: 1.2 // Default 1.2 jika tidak ada data
                    val gender = document.getString("gender") ?: "male" // Default male jika tidak ada data

                    // Hitung total kebutuhan kalori
                    val bmr = if (gender == "male") {
                        66.5 + (13.75 * weight) + (5.003 * height) - (6.75 * age)
                    } else {
                        655.1 + (9.563 * weight) + (1.850 * height) - (4.676 * age)
                    }
                    val totalCalories = bmr * activity

                    // Hitung batas konsumsi berdasarkan kalori
                    val maxSaturatedFat = 0.1 * totalCalories / 9 // 10% dari total kalori, 1 gram = 9 kalori

                    val maxIntake = mapOf(
                        "glukosa" to 50.0, // gram
                        "natrium" to 5.0, // gram
                        "lemak" to 67.0, // gram
                        "lemakJenuh" to maxSaturatedFat, // gram
                        "kolesterol" to 200.0, // mg
                        "kalori" to totalCalories // kcal
                    )

                    _maxDailyIntake.value = maxIntake
                }
            }
            .addOnFailureListener { e ->
                println("Gagal mengambil data pengguna: ${e.localizedMessage}")
            }
    }
}