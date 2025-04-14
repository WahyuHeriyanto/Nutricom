package org.wahyuheriyanto.nutricom.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.wahyuheriyanto.nutricom.data.model.LoginItem
import org.wahyuheriyanto.nutricom.data.model.UserItem

// Kelas AuthViewModel
class AuthViewModel : ViewModel() {
    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState

    private val _uid = MutableStateFlow("")
    val uid: StateFlow<String> = _uid


    private val _fullName = MutableStateFlow("")
    private val _userName = MutableStateFlow("")
    private val _email = MutableStateFlow("")
    private val _phone = MutableStateFlow("")
    private val _date = MutableStateFlow("")
    private val _gender = MutableStateFlow("")
    private val _status = MutableStateFlow(false)

    val fullName: StateFlow<String> = _fullName
    val userName: StateFlow<String> = _userName
    val email: StateFlow<String> = _email
    val phone: StateFlow<String> = _phone
    val date: StateFlow<String> = _date
    val gender: StateFlow<String> = _gender
    val statusLogin: StateFlow<Boolean> = _status



    fun login(userItem : LoginItem) {
        _loginState.update { LoginState.Loading }
        performLogin(this, userItem.email, userItem.password) // Kirim instance viewModel saat memanggil fungsi
    }

    fun loginWithGoogle(idToken: String) {
        _loginState.update { LoginState.Loading }
        performGoogleSignIn(this, idToken) // Kirim instance viewModel saat memanggil fungsi
    }


    fun register(userItem : UserItem) {
        _loginState.update { LoginState.Loading }
        performRegister(this,
            userItem.email,
            userItem.password,
            userItem.fullName,
            userItem.userName,
            userItem.phoneNumber,
            userItem.gender,
            userItem.dateOfBirth) // Kirim instance viewModel saat memanggil fungsi
    }


    fun setLoginState(state: LoginState) {
        _loginState.value = state
    }

    fun setUidCurrent(currentUid: String){
        _uid.value = currentUid
    }

    fun updateData(loginStatus : Boolean,
                   fullNameValue : String,
                   nameValue : String,
                   emailValue: String,
                   phoneValue: String,
                   dateValue: String){
        _status.value = loginStatus
        _fullName.value = fullNameValue
        _userName.value = nameValue
        _email.value = emailValue
        _phone.value = phoneValue
        _date.value = dateValue
    }


}

// Deklarasi expect untuk fungsi login dan register di luar kelas
expect fun performLogin(viewModel: AuthViewModel, email: String, password: String)

expect fun performRegister(viewModel: AuthViewModel, email: String, password: String, name :String, user : String, phone : String, gender:String, birth : String)


expect fun performGoogleSignIn(viewModel: AuthViewModel, idToken: String)

expect fun performLogout(viewModel: AuthViewModel)

// CommonMain: Define the expect function for Google Sign-In



// Definisi status login/registrasi
sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    data class Success(val message: String) : LoginState()
    data class Error(val message: String) : LoginState()
}

