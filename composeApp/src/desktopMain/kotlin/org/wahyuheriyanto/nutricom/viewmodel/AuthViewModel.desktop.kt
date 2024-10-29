package org.wahyuheriyanto.nutricom.viewmodel





actual fun performLogin(
    viewModel: AuthViewModel,
    email: String,
    password: String
) {
}



actual fun performGoogleSignIn(
    viewModel: AuthViewModel, idToken: String
) {
}

actual fun performRegister(
    viewModel: AuthViewModel,
    email: String,
    password: String,
    name: String,
    user: String,
    phone: String,
    birth: String
) {
}