package org.wahyuheriyanto.nutricom.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Kelas AuthViewModel
class AuthViewModel {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        _loginState.update { LoginState.Loading }
        performLogin(email, password)  // Memanggil fungsi expect
    }

    fun register(email: String, password: String) {
        _loginState.update { LoginState.Loading }
        CoroutineScope(Dispatchers.Default).launch {  // Menggunakan Dispatchers.Default untuk Multiplatform
            try {
                performRegister(email, password)
                withContext(Dispatchers.Main) {
                    _loginState.update { LoginState.Success("Registration successful!") }  // Pindahkan ke Main thread
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    _loginState.update { LoginState.Error(e.message ?: "Registration failed") }
                }
            }
        }
    }



}

// Deklarasi expect untuk fungsi login dan register di luar kelas
expect fun performLogin(email: String, password: String)
expect fun performRegister(email: String, password: String)

// Definisi status login/registrasi
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val userId: String) : LoginState()
    data class Error(val message: String) : LoginState()
}