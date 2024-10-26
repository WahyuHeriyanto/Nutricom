package org.wahyuheriyanto.nutricom.viewmodel

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

actual fun performLogin(viewModel: AuthViewModel, email: String, password: String) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val authResult = FirebaseAuth.getInstance()
                .signInWithEmailAndPassword(email, password)
                .await()
            // Jika login berhasil
            val user = authResult.user
            viewModel.setLoginState(LoginState.Success("Login successful!"))

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


actual fun performRegister(viewModel: AuthViewModel, email: String, password: String) {
    CoroutineScope(Dispatchers.IO).launch {
        try {
            val authResult = FirebaseAuth.getInstance()
                .createUserWithEmailAndPassword(email, password)
                .await()

            // Jika registrasi berhasil
            viewModel.setLoginState(LoginState.Success("Registration successful!"))

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


