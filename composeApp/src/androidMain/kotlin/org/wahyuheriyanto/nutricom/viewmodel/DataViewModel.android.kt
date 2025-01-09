package org.wahyuheriyanto.nutricom.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.wahyuheriyanto.nutricom.model.Article

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

//actual fun fetchLastestArticle(viewModelTwo: DataViewModel) {
//    CoroutineScope(Dispatchers.IO).launch {
//        val storageRef = FirebaseStorage.getInstance().reference.child("images")
//        val firestore = FirebaseFirestore.getInstance()
//        val imageList = mutableListOf<String>()
//        val imageList2 = mutableListOf<String>()
//        val imageList3 = mutableListOf<String>()
//        val imageList4 = mutableListOf<String>()
//        val imageList5 = mutableListOf<Long>()
//
//        firestore.collection("articles") // Misalnya, koleksi tempat Anda menyimpan referensi gambar
//            .get()
//            .addOnSuccessListener { documents ->
//                for (document in documents) {
//                    val title = document.getString("title") ?: ""
//                    val author = document.getString("author") ?: ""
//                    val content = document.getString("content") ?: ""
//                    val imageUrl = document.getString("imageUrl") ?: ""
////                    val timestamp = document.getLong("timestamp") ?: 0L // Dapatkan URL gambar
//                    if (imageUrl != null) {
//                        imageList.add(title)
//                        imageList2.add(author)
//                        imageList3.add(content)
//                        imageList4.add(imageUrl)
////                        imageList5.add(timestamp)
//                    }
//                }
//                Log.e("CekList","$imageList" )
//                Log.e("CekList","$imageList2" )
//                Log.e("CekList","$imageList3" )
//                Log.e("CekList","$imageList4" )
////                Log.e("CekList","$imageList5" )
//
//                viewModelTwo.updateImage(imageList)
//                Log.e("OutputImage", "Image link : $imageList")
//            }
//            .addOnFailureListener { exception ->
//                // Tangani jika ada kegagalan
//            }
//    }
//}

