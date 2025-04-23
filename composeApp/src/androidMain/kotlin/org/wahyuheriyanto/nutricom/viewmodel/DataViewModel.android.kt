package org.wahyuheriyanto.nutricom.viewmodel

import android.annotation.SuppressLint
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.wahyuheriyanto.nutricom.data.model.Article
import org.wahyuheriyanto.nutricom.data.model.ConsumtionItem
import org.wahyuheriyanto.nutricom.data.model.RecommenderItem
import org.wahyuheriyanto.nutricom.data.model.ScreeningItem
import org.wahyuheriyanto.nutricom.data.model.UserHealth
import org.wahyuheriyanto.nutricom.data.model.UserProfile

actual fun performData(viewModel: AuthViewModel, viewModelTwo: DataViewModel){
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    CoroutineScope(Dispatchers.IO).launch {
        try {
            Log.e("CekUidSekarang", "Point : $userId")
            userId?.let { currentUser ->
                val uids = currentUser
                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("datas").document(uids).get()
                    .addOnSuccessListener { document ->
                        Log.e("CekUidSekarang", "Point : $uids")
                        val weightValue = document.getLong("weight") ?: 0L
                        val heightValue = document.getLong("height") ?: 0L
                        val calorieValue = document.getLong("calorie") ?: 0L
                        val bmiValue = document.getLong("bmi") ?: 0L
                        when {
                            weightValue != 0L -> {
                                viewModelTwo.updateData(weightValue,heightValue,calorieValue,bmiValue)
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


actual fun fetchImageUrls(viewModelTwo: DataViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        val storageRef = FirebaseStorage.getInstance().reference.child("images")
        val firestore = FirebaseFirestore.getInstance()
        val imageList = mutableListOf<String>()

        firestore.collection("articles") // Misalnya, koleksi tempat Anda menyimpan referensi gambar
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val imageUrl = document.getString("author") // Dapatkan URL gambar
                    if (imageUrl != null) {
                        imageList.add(imageUrl)
                    }
                }

                viewModelTwo.updateImage(imageList)
            }
            .addOnFailureListener { exception ->
                // Tangani jika ada kegagalan
            }
    }
}

actual fun fetchLastestArticle(viewModelTwo: DataViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        Log.e("CekList","Pass 1")
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("articles")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .limit(5)
            .get()
            .addOnSuccessListener { documents ->
                val articleList = documents.map { doc ->
                    Article(
                        title = doc.getString("title") ?: "",
                        author = doc.getString("author") ?: "",
                        content = doc.getString("content") ?: "",
                        imageUrl = doc.getString("imageUrl") ?: "",
//                        timestamp = doc.getLong("timestamp") ?: 0L
                    )
                }
//                Log.e("CekList","$articleList")
                viewModelTwo.updateArticle(articleList)
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }
    }
}

actual fun fetchRecommender(viewModelTwo: DataViewModel){
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    CoroutineScope(Dispatchers.IO).launch {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("recommendations").document(userId)
            .collection("active")
            .get()
            .addOnSuccessListener { document ->
                val recommenderList = document.map {doc ->
                    RecommenderItem(
                        id = doc.id,
                        imageUrl = doc.getString("sentence") ?: "",
                        sentence = doc.getString("sentence") ?: ""
                    )
                }
                viewModelTwo.updateRecommender(recommenderList)
            }
            .addOnFailureListener { exception ->

            }
    }
}

actual fun fetchScreningResult(viewModelTwo: DataViewModel) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    CoroutineScope(Dispatchers.IO).launch {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("screening")
            .document(userId)
            .collection("active")
            .get()
            .addOnSuccessListener { documents ->
                val screeningList = documents.map { doc ->
                    ScreeningItem(
                        id = doc.id,
                        type = doc.getString("type") ?: "",
                        imageUrl = doc.getString("imageUrl") ?: "",
                        timestamp = doc.getLong("timestamp") ?: 0L,
                    )
                }
                Log.e("tesscreen","$screeningList")
                viewModelTwo.updateScreening(screeningList)
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }
    }
}

actual fun fetchAllArticle(viewModelTwo: DataViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        Log.e("CekList","Pass 1")
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("articles")
            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener { documents ->
                val articleList = documents.map { doc ->
                    Article(
                        title = doc.getString("title") ?: "",
                        author = doc.getString("author") ?: "",
                        content = doc.getString("content") ?: "",
                        imageUrl = doc.getString("imageUrl") ?: "",
//                        timestamp = doc.getLong("timestamp") ?: 0L
                    )
                }
//                Log.e("CekList","$articleList")
                viewModelTwo.updateArticle(articleList)
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }
    }
}

actual fun fetchNutricions(viewModelTwo: DataViewModel) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    CoroutineScope(Dispatchers.IO).launch {
        try {
            userId?.let { currentUser ->
                val uids = currentUser
                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("nutricions").document(uids).get()
                    .addOnSuccessListener { doc ->
                        val calories = doc.getLong("kalori") ?: 0L
                        val fat = doc.getLong("lemak") ?: 0L
                        val salt = doc.getLong("natrium") ?: 0L
                        val saturatedFat = doc.getLong("lemakJenuh") ?: 0L
                        val cholesterol = doc.getLong("kolesterol") ?: 0L
                        val sugars = doc.getLong("glukosa")?: 0L
                        when {
                            calories != 0L -> {
                                viewModelTwo.updateNutricion(calories,sugars,fat,saturatedFat,salt,cholesterol)
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

actual fun performDataLogin(
    viewModel: AuthViewModel,
    viewModelTwo: DataViewModel,
    uid: String?
) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            Log.e("cekisiuid","$uid")
            uid?.let { currentUser ->
                val uids = currentUser
                Log.e("cekisiuid","$uids")
                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("datas").document(uids).get()
                    .addOnSuccessListener { document ->
                        val weightValue = document.getLong("weight") ?: 0L
                        val heightValue = document.getLong("height") ?: 0L
                        val calorieValue = document.getLong("calorie") ?: 0L
                        val bmiValue = document.getLong("bmi") ?: 0L
                        when {
                            weightValue != 0L -> {
                                Log.e("cekisiuid","weight value $weightValue $heightValue")
                                viewModelTwo.updateData(weightValue,heightValue,calorieValue,bmiValue)
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

actual fun fetchConsumtion(viewModelTwo: DataViewModel) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    CoroutineScope(Dispatchers.IO).launch {
        Log.e("CekList","Pass 1")
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("consume")
            .document(userId)
            .collection("food")
            .get()
            .addOnSuccessListener { documents ->
                val consumeList = documents.map { doc ->
                    ConsumtionItem(
                        id = doc.id,
                        imageUrl = doc.getString("barcode") ?: "",
                        name = doc.getString("name")?: "",
                        calories = doc.getLong("calories")?: 0L,
                        cholesterol = doc.getLong("cholesterol")?: 0L,
                        fat = doc.getLong("fat")?:0L,
                        saturatedFat = doc.getLong("saturatedFat")?: 0L,
                        sugars = doc.getLong("sugars")?: 0L,
                        salt = doc.getLong("salt") ?: 0L

                        )
                }
                Log.e("tesscreen","$consumeList")
                viewModelTwo.updateConsumtion(consumeList)
            }
            .addOnFailureListener { exception ->
                // Handle failure
            }
    }
}

actual fun deleteConsumtion(viewModelTwo: DataViewModel, itemId: String) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    CoroutineScope(Dispatchers.IO).launch {
        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("consume")
            .document(userId)
            .collection("food")
            .document(itemId) // ← ini dari doc.id
            .delete()

//        firestore.collection("consume")
//            .document(userId)
//            .collection("food")
//            .whereEqualTo("name", itemName)
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    firestore.collection("consume")
//                        .document(userId)
//                        .collection("food")
//                        .document(document.id)
//                        .delete()
//                }
//            }
//            .addOnFailureListener { e ->
//                Log.e("DeleteItem", "Error deleting item: $e")
//            }
    }

}

//RecListActive in RecommenderList
fun deleteRecommenderItem( itemId: String) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val firestore = FirebaseFirestore.getInstance()
    firestore.collection("recommendations").document(userId)
        .collection("active").document(itemId)
        .delete()
}

//DataDiriScreen
fun fetchUserProfile(onResult: (UserProfile) -> Unit) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val firestore = FirebaseFirestore.getInstance()

    firestore.collection("users").document(uid).get()
        .addOnSuccessListener { userDoc ->
            firestore.collection("datas").document(uid).get()
                .addOnSuccessListener { dataDoc ->
                    val profile = UserProfile(
                        imageUrl = userDoc.getString("imageUrl") ?: "",
                        fullName = userDoc.getString("fullName") ?: "",
                        userName = userDoc.getString("userName") ?: "",
                        gender = userDoc.getString("gender") ?: "",
                        email = userDoc.getString("email") ?: "",
                        dateOfBirth = userDoc.getString("dateOfBirth") ?: "",
                        phoneNumber = userDoc.getString("phoneNumber") ?: "",
                        age = (dataDoc.getLong("age") ?: 0).toString()
                    )
                    onResult(profile)
                }
        }
}

//DataDiriScreen
fun updateUserProfile(profile: UserProfile, onSuccess: () -> Unit) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val firestore = FirebaseFirestore.getInstance()

    val userUpdate = mapOf(
        "imageUrl" to profile.imageUrl,
        "fullName" to profile.fullName,
        "userName" to profile.userName,
        "gender" to profile.gender,
        "email" to profile.email,
        "dateOfBirth" to profile.dateOfBirth,
        "phoneNumber" to profile.phoneNumber
    )

    val dataUpdate = mapOf(
        "age" to profile.age.toLong(),
    )

    firestore.collection("users").document(uid).update(userUpdate)
    firestore.collection("datas").document(uid).update(dataUpdate)
        .addOnSuccessListener { onSuccess() }
}

//First screening in NewScreeningScreen
fun submitScreening(inputs: List<Float>) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val firestore = FirebaseFirestore.getInstance()

    if (inputs.size < 8) return

    val age = inputs[0]
    val weight = inputs[1]
    val height = inputs[2]
    val smokingHistory = inputs[3]
    val alcoholConsume = inputs[4]
    val heartDisease = inputs[5]
    val diabetesDisease = inputs[6]
    val activity = inputs[7]

    val heightMeter = height / 100f
    val bmi = if (heightMeter != 0f) weight / (heightMeter * heightMeter) else 0f

    val userUpdate = mapOf(
        "age" to age,
        "weight" to weight,
        "height" to height,
        "smokingHistory" to smokingHistory,
        "alcoholConsume" to alcoholConsume,
        "heartDisease" to heartDisease,
        "diabetesDisease" to diabetesDisease,
        "activity" to activity,
        "bmi" to bmi
    )
    val status = mapOf(
        "newUser" to false
    )
    //update kesehatan pengguna
    firestore.collection("datas")
        .document(uid)
        .set(userUpdate)
        .addOnSuccessListener {
            Log.d("Screening", "Data berhasil disimpan")
        }
        .addOnFailureListener {
            Log.e("Screening", "Gagal menyimpan data: ${it.message}")
        }
    //update status pengguna baru
    firestore.collection("users")
        .document(uid)
        .update(status)
        .addOnSuccessListener {
            Log.d("Screening", "Data berhasil disimpan")
        }
        .addOnFailureListener {
            Log.e("Screening", "Gagal menyimpan data: ${it.message}")
        }
}

@SuppressLint("SuspiciousIndentation")
fun fetchDataHealth(onResult: (UserHealth) -> Unit) {
    val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val firestore = FirebaseFirestore.getInstance()
            firestore.collection("datas")
                .document(uid)
                .get()
                .addOnSuccessListener { dataDoc ->
                    val health = UserHealth(
                        activity =(dataDoc.getLong("activity") ?: 0) ,
                        age = (dataDoc.getLong("age") ?: 0) ,
                        alcoholConsume = (dataDoc.getLong("alcoholConsum") ?: 0) ,
                        apHi = (dataDoc.getLong("apHi") ?: 0) ,
                        apLo = (dataDoc.getLong("apLo") ?: 0) ,
                        bmi = (dataDoc.getLong("bmi") ?: 0) ,
                        cardio = (dataDoc.getLong("cardio") ?: 0) ,
                        cholesterol = (dataDoc.getLong("cholesterol") ?: 0) ,
                        diabetes = (dataDoc.getLong("diabetes") ?: 0) ,
                        gluc = (dataDoc.getLong("gluc") ?: 0) ,
                        hba1c = (dataDoc.getLong("hba1c") ?: 0) ,
                        hdl = (dataDoc.getLong("hdl") ?: 0) ,
                        heartDisease = (dataDoc.getLong("heartDisease") ?: 0) ,
                        height = (dataDoc.getLong("height") ?: 0) ,
                        ldl = (dataDoc.getLong("ldl") ?: 0) ,
                        sleep = (dataDoc.getLong("sleep") ?: 0) ,
                        smokingHistory = (dataDoc.getLong("smokingHistory") ?: 0) ,
                        tri = (dataDoc.getLong("tri") ?: 0) ,
                        weight = (dataDoc.getLong("weight") ?: 0) ,
                        healthComplaint = (dataDoc.getString("healthComplaint")?: "")
                    )
                    onResult(health)
                }
}


