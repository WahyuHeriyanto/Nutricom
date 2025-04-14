package org.wahyuheriyanto.nutricom.viewmodel

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

actual fun performLogin(viewModel: AuthViewModel, email: String, password: String) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val authResult = FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .await()

            val user = authResult.user
            user?.let { currentUser ->
                val uid = currentUser.uid
                val firestore = FirebaseFirestore.getInstance()
                firestore.collection("users").document(uid).get()
                    .addOnSuccessListener { document ->
                        val statusNewMember = document.getBoolean("newUser") ?: false
                        val nameValue = document.getString("userName") ?: ""
                        val fullNameValue = document.getString("fullName")?: ""
                        val emailValue = document.getString("email")?: ""
                        val phoneValue = document.getString("phoneNumber") ?: ""
                        val dateValue = document.getString("dateOfBirth") ?: ""

                        when {
                            nameValue != "" -> {
                                viewModel.setLoginState(LoginState.Success("Login successful!"))
                                viewModel.updateData(statusNewMember, fullNameValue,nameValue,emailValue,phoneValue,dateValue) // Perbarui points di ViewModel
                                viewModel.setUidCurrent(uid)

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
                           gender : String,
                           birth : String) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val authResult = FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .await()

            val users = authResult.user

              // Jika registrasi berhasil
            if (users != null) {
                // Data pengguna yang akan disimpan di Firestore
                val userData = hashMapOf(
                    "email" to email,
                    "fullName" to name,
                    "userName" to user,
                    "phoneNumber" to phone,
                    "dateOfBirth" to birth,
                    "gender" to gender,
                    "newUser" to true
                )
                val healthData = hashMapOf(
                    "height" to 0L,
                    "weight" to 0L,
                    "bmi" to 0L
                )
                val nutricionData = hashMapOf(
                    "kalori" to 0L,
                    "glukosa" to 0L,
                    "lemak" to 0L,
                    "natrium" to 0L
                )
                // Simpan data ke Firestore
                FirebaseFirestore.getInstance()
                    .collection("users")
                    .document(users.uid)
                    .set(userData)
                    .await()

                FirebaseFirestore.getInstance()
                    .collection("datas")
                    .document(users.uid)
                    .set(healthData)
                    .await()

                FirebaseFirestore.getInstance()
                    .collection("nutricions")
                    .document(users.uid)
                    .set(nutricionData)
                    .await()

                viewModel.setLoginState(LoginState.Success("Registration successful!"))
            } else {
                Log.e("ErrorRegis","Masih error")
            }

        } catch (e: Exception) {
            viewModel.setLoginState(LoginState.Error("Registration failed"))
        }
    }
}




actual fun performGoogleSignIn(viewModel: AuthViewModel, idToken: String) {
    val credential = GoogleAuthProvider.getCredential(idToken, null)
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val authResult = FirebaseAuth.getInstance().signInWithCredential(credential).await()
            val user = authResult.user
            if (user != null) {
                val userId = user.uid
                val name = user.displayName ?: "Unknown"
                val email = user.email ?: "No Email"

                // Cek apakah data pengguna sudah ada di Firestore
                val firestore = FirebaseFirestore.getInstance()
                val userDoc = firestore.collection("users").document(userId).get().await()

                if (!userDoc.exists()) {
                    // Jika belum ada, buat data baru
                    val userData = hashMapOf(
                        "email" to email,
                        "fullName" to name,
                        "userName" to "",
                        "phoneNumber" to "",
                        "dateOfBirth" to "",
                        "gender" to ""
                    )

                    val healthData = hashMapOf(
                        "height" to 0L,
                        "weight" to 0L,
                        "bmi" to 0L
                    )

                    val nutricionData = hashMapOf(
                        "kalori" to 0L,
                        "glukosa" to 0L,
                        "lemak" to 0L,
                        "natrium" to 0L
                    )

                    // Simpan data ke Firestore
                    firestore.collection("users").document(userId).set(userData).await()
                    firestore.collection("datas").document(userId).set(healthData).await()
                    firestore.collection("nutricions").document(userId).set(nutricionData).await()
                }

                viewModel.setLoginState(LoginState.Success("Login successful!"))
            } else {
                viewModel.setLoginState(LoginState.Error("Google Sign-In failed"))
            }
        } catch (e: Exception) {
            viewModel.setLoginState(LoginState.Error("Login failed: ${e.message}"))
        }
    }
}


actual fun performLogout(viewModel: AuthViewModel) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val uid = ""
            viewModel.setLoginState(LoginState.Idle)
            viewModel.setUidCurrent(uid)
        } catch (e: Exception){
            Log.e("logout","gagal logout")
        }
    }
}