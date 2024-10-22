package org.wahyuheriyanto.nutricom.viewmodel

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

actual fun performLogin(viewModel: AuthViewModel, email: String, password: String) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val authResult = FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .await()

            // Jika login berhasil
            val user = authResult.user
            viewModel.setLoginState(LoginState.Success("Login successful!"))

        } catch (e: Exception) {
            // Jika login gagal
            viewModel.setLoginState(LoginState.Error("Email dan password tidak terdaftar"))
        }
    }
}


actual fun performRegister(email: String, password: String) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val authResult = FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .await()
            val user = authResult.user
            // Anda bisa mengirimkan hasil registrasi ke UI dengan cara mengupdate state
        } catch (e: Exception) {
            // Tangani error
        }
    }
}