package org.wahyuheriyanto.nutricom.viewmodel

import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore


data class DiabetesInputData(
    val gender: Double,
    val age: Double,
    val hypertension: Double,
    val heartDisease: Double,
    val smokingHistory: Double,
    val bmi: Double,
    val hbA1c: Double,
    val bloodGlucose: Double
)


data class CardioInputData(
    val age: Double,
    val gender: Double,
    val height: Double,
    val weight: Double,
    val apHi: Double,
    val apLo: Double,
    val cholesterol: Double,
    val gluc: Double,
    val smoke: Double,
    val alco: Double,
    val active: Double
)

data class UmumInputData(
    val height: Double,
    val weight: Double,
    val bmi: Double,
    val apHi: Double,
    val apLo: Double,
    val cardio: Double,
    val diabetes: Double,
    val cholesterol: Double,
    val gluc: Double,
    val smoke: Double,
    val alco: Double,
    val active: Double,
    val sleep: Double,
    val ldl: Double,
    val hdl: Double,
    val tri: Double,
    val hba1c: Double,
    val healthComplaint: String
)

class DataPredictViewModel : ViewModel() {

    private val db = FirebaseFirestore.getInstance()

    // Data sementara
    private var _inputData: DiabetesInputData? = null
    private var _inputDataTwo: CardioInputData? = null
    private var _inputDataThree: UmumInputData? = null

    fun saveInputDataUmum(input: UmumInputData){
        _inputDataThree = input
    }
    fun saveInputDataDiabetes(input: DiabetesInputData) {
        _inputData = input
    }

    fun saveInputDataCardio(input: CardioInputData){
        _inputDataTwo = input
    }

    // Simpan ke Firestore
    fun sendDataUmumToFirestore(input: UmumInputData, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val heightMeter = input.height / 100f
        val bmi = if (heightMeter.toFloat() != 0f) input.weight / (heightMeter * heightMeter) else 0f

        val newDocRef = db.collection("screening")
            .document(userId)
            .collection("active")
            .document()


        val dataMap = hashMapOf(
            "id" to newDocRef.id,
            "type" to "Umum",
            "imageUrl" to "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/diabetes_pictures.jpg?alt=media&token=ea1a095b-8105-4a64-ba88-44a2e7451fc9",
            "height" to input.height,
            "weight" to input.weight,
            "bmi" to bmi,
            "apHi" to input.apHi,
            "apLo" to input.apLo,
            "cardio" to input.cardio,
            "diabetes" to input.diabetes,
            "cholesterol" to input.cholesterol,
            "gluc" to input.gluc,
            "smoke" to input.smoke,
            "alco" to input.alco,
            "active" to input.active,
            "sleep" to input.sleep,
            "ldl" to input.ldl,
            "hdl" to input.hdl,
            "tri" to input.tri,
            "hba1c" to input.hba1c,
            "healthComplaint" to input.healthComplaint,
            "timestamp" to System.currentTimeMillis()
        )

        val dataMapTwo = mapOf(
            "height" to input.height,
            "weight" to input.weight,
            "bmi" to bmi,
            "apHi" to input.apHi,
            "apLo" to input.apLo,
            "cardio" to input.cardio,
            "diabetes" to input.diabetes,
            "cholesterol" to input.cholesterol,
            "gluc" to input.gluc,
            "smokingHistory" to input.smoke,
            "alcoholConsume" to input.alco,
            "active" to input.active,
            "sleep" to input.sleep,
            "ldl" to input.ldl,
            "hdl" to input.hdl,
            "tri" to input.tri,
            "hba1c" to input.hba1c,
            "healthComplaint" to input.healthComplaint
        )

        newDocRef.set(dataMap)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }

//        db.collection("screening")
//            .document(userId)
//            .collection("active")
//            .add(dataMap)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { onError(it) }

        db.collection("datas")
            .document(userId)
            .update(dataMapTwo)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }
    }

    fun sendDataDiabetesToFirestore(result : Int?, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val input = _inputData ?: return

        val newDocRef = db.collection("screening")
            .document(userId)
            .collection("active")
            .document()

        val dataMap = hashMapOf(
            "id" to newDocRef.id,
            "type" to "Diabetes",
            "imageUrl" to "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/diabetes_pictures.jpg?alt=media&token=ea1a095b-8105-4a64-ba88-44a2e7451fc9",
            "gender" to input.gender,
            "age" to input.age,
            "hypertension" to input.hypertension,
            "heartDisease" to input.heartDisease,
            "smokingHistory" to input.smokingHistory,
            "bmi" to input.bmi,
            "hbA1c" to input.hbA1c,
            "bloodGlucose" to input.bloodGlucose,
            "prediction" to if (result == 0) "Beresiko Diabetes" else "Tidak Beresiko Diabetes",
            "timestamp" to System.currentTimeMillis()
        )

        newDocRef.set(dataMap)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }

//        db.collection("screening")
//            .document(userId)
//            .collection("active")
//            .add(dataMap)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { onError(it) }
    }

    fun sendDataCardioToFirestore(result : Int?, onSuccess: () -> Unit, onError: (Exception) -> Unit) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val input = _inputDataTwo ?: return
//        val resultPredict : Int? = _predictionResult ?: return
        //${age}/${gender}/${height}/${weight}/${apHi}/${apLo}/${cholesterol}/${gluc}/${smoke}/${alco}/${active}

        val newDocRef = db.collection("screening")
            .document(userId)
            .collection("active")
            .document()


        val dataMap = hashMapOf(
            "id" to newDocRef.id,
            "type" to "Kardiovaskular",
            "imageUrl" to "https://firebasestorage.googleapis.com/v0/b/nutricom-41968.firebasestorage.app/o/kardiovaskular_pictures.png?alt=media&token=5b35c44c-ba17-46b7-843b-9e2c90d9e5a1",
            "age" to input.age,
            "gender" to input.gender,
            "height" to input.height,
            "weight" to input.weight,
            "apHi" to input.apHi,
            "apLo" to input.apLo,
            "cholesterol" to input.cholesterol,
            "gluc" to input.gluc,
            "smoke" to input.smoke,
            "alco" to input.alco,
            "active" to input.active,
            "prediction" to if (result == 0) "Beresiko Kardiovaskular" else "Tidak Beresiko Kardiovaskular",
            "timestamp" to System.currentTimeMillis()
        )

        newDocRef.set(dataMap)
            .addOnSuccessListener { onSuccess() }
            .addOnFailureListener { onError(it) }

//        db.collection("screening")
//            .document(userId)
//            .collection("active")
//            .add(dataMap)
//            .addOnSuccessListener { onSuccess() }
//            .addOnFailureListener { onError(it) }
    }
}

