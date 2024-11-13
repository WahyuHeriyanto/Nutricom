package org.wahyuheriyanto.nutricom.viewmodel

import android.content.Context
import android.util.Log
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.wahyuheriyanto.nutricom.data.DataStoreUtils
import org.wahyuheriyanto.nutricom.model.UserItem

actual fun performLogin(viewModel: AuthViewModel, email: String, password: String) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val authResult = FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .await()
            // Jika login berhasil
            val user = authResult.user
            user?.let { currentUser ->
                val uid = currentUser.uid
                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("users").document(uid).get()
                    .addOnSuccessListener { document ->
                        val pointsValue = document.getLong("point") ?: 0L
                        val nameValue = document.getString("userName") ?: ""
                        val fullNameValue = document.getString("fullName")?: ""
                        val emailValue = document.getString("email")?: ""
                        val phoneValue = document.getString("phoneNumber") ?: ""
                        val dateValue = document.getString("dateOfBirth") ?: ""

                        when {
                            pointsValue != 0L -> {
                                viewModel.setLoginState(LoginState.Success("Login successful!"))
                                viewModel.updatePoints(pointsValue,fullNameValue,nameValue,emailValue,phoneValue,dateValue) // Perbarui points di ViewModel
                                viewModel.setUidCurrent(uid)
                                Log.e("CekPoint", "Point : $pointsValue")

                            }
                            else -> {
                                Log.e("Error","Belum keisi")
                            }
                        }
                    }
            }


        } catch (e: FirebaseAuthInvalidCredentialsException) {
            // Tangani error kredensial yang salah
            viewModel.setLoginState(LoginState.Error("Error: Incorrect email and password"))
        } catch (e: Exception) {
            // Jika login gagal
            withContext(Dispatchers.Main) {
                viewModel.setLoginState(LoginState.Error("Terjadi kesalahan saat login"))
            }
        }
    }
}


actual fun performRegister(viewModel: AuthViewModel,
                           email: String,
                           password: String,
                           name :String,
                           user : String,
                           phone : String,
                           birth : String) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val authResult = FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .await()

            val users = authResult.user

            val point: Int = UserItem().point


              // Jika registrasi berhasil
            if (users != null) {
                // Data pengguna yang akan disimpan di Firestore
                val userData = hashMapOf(
                    "email" to email,
                    "password" to password,
                    "fullName" to name,       // Data dari input user
                    "userName" to user,
                    "phoneNumber" to phone,
                    "dateOfBirth" to birth,
                    "point" to point // Pertimbangkan hashing
                )

                // Simpan data ke Firestore
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(users.uid)
                    .set(userData)
                    .await()



                viewModel.setLoginState(LoginState.Success("Registration successful!"))
            } else {
                Log.e("ErrorRegis","Masih error")
            }


        } catch (e: Exception) {
            // Jika registrasi gagal
            viewModel.setLoginState(LoginState.Error("Registration failed"))
        }
    }
}




actual fun performGoogleSignIn(viewModel: AuthViewModel, idToken: String) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    CoroutineScope(Dispatchers.IO).launch {
        try {
            FirebaseAuth.getInstance().signInWithCredential(credential).await()
            viewModel.setLoginState(LoginState.Success("Login successful!"))
        } catch (e: Exception) {
            viewModel.setLoginState(LoginState.Error("Login failed: ${e.message}"))
        }
    }
}


