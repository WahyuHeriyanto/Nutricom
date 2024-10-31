package org.wahyuheriyanto.nutricom.viewmodel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.wahyuheriyanto.nutricom.model.LoginItem
import org.wahyuheriyanto.nutricom.model.UserItem

// Kelas AuthViewModel
class AuthViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState


    private val _points = MutableStateFlow(0L) // StateFlow untuk nilai points
    val points: StateFlow<Long> = _points


    fun login(userItem : LoginItem) {
        _loginState.update { LoginState.Loading }
        performLogin(this, userItem.email, userItem.password) // Kirim instance viewModel saat memanggil fungsi
    }

    fun loginWithGoogle(idToken: String) {
        _loginState.update { LoginState.Loading }
        performGoogleSignIn(this, idToken) // Kirim instance viewModel saat memanggil fungsi
    }


    fun register(userItem : UserItem ) {
        _loginState.update { LoginState.Loading }
        performRegister(this,
            userItem.email,
            userItem.password,
            userItem.fullName,
            userItem.userName,
            userItem.phoneNumber,
            userItem.dateOfBirth) // Kirim instance viewModel saat memanggil fungsi
    }


    fun setLoginState(state: LoginState) {
        _loginState.value = state
    }



    fun updatePoints(newPoints: Long) {
        _points.value = newPoints
    }


}

// Deklarasi expect untuk fungsi login dan register di luar kelas
expect fun performLogin(viewModel: AuthViewModel, email: String, password: String)

expect fun performRegister(viewModel: AuthViewModel, email: String, password: String, name :String, user : String, phone : String, birth : String)

expect fun performGoogleSignIn(viewModel: AuthViewModel, idToken: String)

// CommonMain: Define the expect function for Google Sign-In



// Definisi status login/registrasi
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val message: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

