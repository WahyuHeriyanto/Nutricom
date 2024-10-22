package org.wahyuheriyanto.nutricom.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

// Kelas AuthViewModel
class AuthViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    fun login(email: String, password: String) {
        _loginState.update { LoginState.Loading }
        performLogin(this, email, password) // Kirim instance viewModel saat memanggil fungsi
    }



    fun register(email: String, password: String) {
        _loginState.update { LoginState.Loading }
        performRegister(this, email, password) // Kirim instance viewModel saat memanggil fungsi
    }


    fun setLoginState(state: LoginState) {
        _loginState.value = state
    }
}

// Deklarasi expect untuk fungsi login dan register di luar kelas
expect fun performLogin(viewModel: AuthViewModel, email: String, password: String)

expect fun performRegister(viewModel: AuthViewModel, email: String, password: String)

// Definisi status login/registrasi
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val message: String) : LoginState()
    data class Error(val message: String) : LoginState()
}