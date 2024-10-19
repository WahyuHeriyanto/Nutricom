package org.wahyuheriyanto.nutricom.viewmodel

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

// Kelas AuthViewModel
class AuthViewModel {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        _loginState.update { LoginState.Loading }
        performLogin(email, password)  // Memanggil fungsi expect
    }
}

// Deklarasi expect untuk fungsi login di luar kelas
expect fun performLogin(email: String, password: String)

// Definisi status login
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val userId: String) : LoginState()
    data class Error(val message: String) : LoginState()
}
