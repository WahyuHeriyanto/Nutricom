package org.wahyuheriyanto.nutricom.model

data class UserItem(
    val email: String = "",
    val password: String = "",
    val fullName: String = "",
    val userName: String = "",
    val phoneNumber: String = "",
    val dateOfBirth: String = ""

)

data class LoginItem(
    val email: String = "",
    val password: String = ""
)