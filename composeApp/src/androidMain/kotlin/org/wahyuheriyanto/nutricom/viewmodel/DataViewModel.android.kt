package org.wahyuheriyanto.nutricom.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.wahyuheriyanto.nutricom.model.Article
import org.wahyuheriyanto.nutricom.model.Nutricions
import org.wahyuheriyanto.nutricom.model.RecommenderItem
import org.wahyuheriyanto.nutricom.model.ScreeningItem
import org.wahyuheriyanto.nutricom.view.components.ConsumtionItem
import org.wahyuheriyanto.nutricom.view.components.ScreeningItem

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
//                Log.e("CekList","$imageList")

                viewModelTwo.updateImage(imageList)
//                Log.e("OutputImage", "Image link : $imageList")
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
    CoroutineScope(Dispatchers.IO).launch {
        Log.e("CekList","Pass 1")
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("screening")
//            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
//            .limit(5)
            .get()
            .addOnSuccessListener { documents ->
                val screeningList = documents.map { doc ->
                    ScreeningItem(
                        type = doc.getString("type") ?: "",
                        date = doc.getString("date") ?: "",
                        imageUrl = doc.getString("imageUrl") ?: "",
//                        timestamp = doc.getLong("timestamp") ?: 0L
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
//            .orderBy("timestamp", com.google.firebase.firestore.Query.Direction.DESCENDING)
//            .limit(5)
            .get()
            .addOnSuccessListener { documents ->
                val consumeList = documents.map { doc ->
                    org.wahyuheriyanto.nutricom.model.ConsumtionItem(
                        imageUrl = doc.getString("barcode") ?: "",
                        name = doc.getString("name")?: "",
                        calories = doc.getLong("calories")?: 0L,
                        cholesterol = doc.getLong("cholesterol")?: 0L,
                        fat = doc.getLong("fat")?:0L,
                        saturatedFat = doc.getLong("saturatedFat")?: 0L,
                        sugars = doc.getLong("sugars")?: 0L

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

actual fun deleteConsumtion(viewModelTwo: DataViewModel, itemName: String) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    CoroutineScope(Dispatchers.IO).launch {
        val firestore = FirebaseFirestore.getInstance()
        firestore.collection("consume")
            .document(userId)
            .collection("food")
            .whereEqualTo("name", itemName)
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    firestore.collection("consume")
                        .document(userId)
                        .collection("food")
                        .document(document.id)
                        .delete()
                }
            }
            .addOnFailureListener { e ->
                Log.e("DeleteItem", "Error deleting item: $e")
            }
    }

}

fun deleteRecommenderItem( itemId: String) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val firestore = FirebaseFirestore.getInstance()
    firestore.collection("recommendations").document(userId)
        .collection("active").document(itemId)
        .delete()
}

